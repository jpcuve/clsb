package com.messio.clsb.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.*;
import com.messio.clsb.event.BankEvent;
import com.messio.clsb.event.BaseEvent;
import com.messio.clsb.event.CurrencyEvent;
import com.messio.clsb.model.BankModel;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 10/3/16.
 */
@Singleton(name = "clsb/scheduler")
@LocalBean
@Startup
public class Scheduler {
    public static final Logger LOGGER = Logger.getLogger(Scheduler.class.getCanonicalName());
    @Resource
    private TimerService timerService;
    @Inject
    private ClsbFacade facade;
    @Inject
    private Event<BaseEvent> emitter;
    @Inject
    private PayInManager payInManager;
    @Inject
    private PayOutManager payOutManager;
    @Inject
    private SettlementManager settlementManager;

    private BankModel bankModel;
    private LocalTime localTime = LocalTime.MIN;
    private List<BaseEvent> events;

    @PostConstruct
    public void init() {
        this.events = new ArrayList<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule annotationModule = new JaxbAnnotationModule();
        objectMapper.registerModule(annotationModule);
        final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/bank-01.json");
        try{
            this.bankModel = objectMapper.readValue(is, BankModel.class);
            final Bank bank = facade.build(bankModel);
            events.add(new BankEvent(bank.getOpening(), "opening", bank));
            events.add(new BankEvent(bank.getSettlementCompletionTarget(), "sct", bank));
            events.add(new BankEvent(bank.getClosing(), "closing", bank));
            for (final Currency currency: facade.findCurrencies(bank)){
                events.add(new CurrencyEvent(currency.getOpening(), "opening", currency));
                events.add(new CurrencyEvent(currency.getFundingCompletionTarget(), "fct", currency));
                events.add(new CurrencyEvent(currency.getClose(), "close", currency));
                events.add(new CurrencyEvent(currency.getClosing(), "closing", currency));
            }
        } catch(IOException e){
            LOGGER.severe("cannot read bank model, " + e.getMessage());
        }
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        final Timer timer = timerService.createIntervalTimer(0, 100L, timerConfig);
    }

    @Timeout
    public void timeout(){
        final LocalTime to = localTime.plusMinutes(10);
        events.stream().filter(e -> (localTime.isBefore(e.getWhen()) || localTime.equals(e.getWhen())) && e.getWhen().isBefore(to)).forEach(e -> emitter.fire(e));
        localTime = to;
    }

    public void onBankEvent(@Observes BankEvent event) {
        LOGGER.info(String.format("Bank event: %s", event));
        final Bank bank = bankModel.getBank();
        final Account mirror = facade.findAccount(bank, Account.MIRROR_NAME);
        switch(event.getName()){
            case "opening":
                LOGGER.info(String.format("Bank opening: %s", mirror.getPosition()));
                break;
            case "sct":
                final List<Settlement> settlements = this.bankModel.getInstructions().stream()
                        .filter(i -> i instanceof Settlement && i.getWhen().isBefore(event.getWhen()))
                        .map(i -> (Settlement) i)
                        .collect(Collectors.toList());
                final List<Transfer> queue = settlementManager.buildSettlementQueue(settlements);
                LOGGER.info(String.format("Settlement queue size: %s", queue.size()));
                settlementManager.settleUnconditionally(bank, event.getWhen(), queue);
                break;
            case "closing":
                LOGGER.info(String.format("Bank closing: %s", mirror.getPosition()));
                break;
        }
    }

    public void onCurrencyEvent(@Observes CurrencyEvent event) {
        LOGGER.info(String.format("Currency event: %s", event));
        final String iso = event.getCurrency().getIso();
        final Bank bank = bankModel.getBank();
        switch(event.getName()){
            case "opening":
                break;
            case "fct":
                final List<PayIn> payIns = this.bankModel.getInstructions().stream()
                        .filter(i -> i instanceof PayIn && i.getWhen().isBefore(event.getWhen()))
                        .map(i -> (PayIn) i)
                        .collect(Collectors.toList());
                payInManager.bookPayIns(bank, event.getWhen(), payIns, iso);
                break;
            case "close":
                final List<PayOut> payOuts = payOutManager.computePayOuts(bank, iso);
                payOutManager.bookPayOuts(bank, event.getWhen(), payOuts);
                break;
            case "closing":
                break;
        }
    }

    public BankModel getBankModel() {
        return bankModel;
    }
}
