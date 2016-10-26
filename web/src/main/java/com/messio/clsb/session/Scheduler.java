package com.messio.clsb.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.*;
import com.messio.clsb.event.BankEvent;
import com.messio.clsb.event.BaseEvent;
import com.messio.clsb.event.CurrencyEvent;

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
import java.util.Arrays;
import java.util.Collections;
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
    @Inject
    private AccountManager accountManager;

    private List<BaseEvent> events;
    private Instruction[] instructions;
    private int index;

    @PostConstruct
    public void init() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        if (facade.findBank() == null){
            try (final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/initial.json")) {
                final Bank bank = objectMapper.readValue(is, Bank.class);
                for (Currency currency: bank.getCurrencies()){
                    currency.setBank(bank);
                    facade.create(currency);
                }
                for (Account account: bank.getAccounts()){
                    account.setBank(bank);
                    facade.create(account);
                }
                facade.create(bank);
            } catch(IOException e){
                LOGGER.severe(e.getMessage());
            }
        }

        this.events = new ArrayList<>();
        final Bank bank = facade.findBank();
        events.add(new BankEvent(bank.getOpening(), "opening", bank));
        events.add(new BankEvent(bank.getSettlementCompletionTarget(), "sct", bank));
        events.add(new BankEvent(bank.getClosing(), "closing", bank));
        for (final Currency currency: facade.findCurrencies(bank)){
            events.add(new CurrencyEvent(currency.getOpening(), "opening", currency));
            events.add(new CurrencyEvent(currency.getFundingCompletionTarget(), "fct", currency));
            events.add(new CurrencyEvent(currency.getClose(), "close", currency));
            events.add(new CurrencyEvent(currency.getClosing(), "closing", currency));
        }
        events.sort((e1, e2) -> e1.getWhen().equals(e2.getWhen()) ? 0 : (e1.getWhen().isAfter(e2.getWhen()) ? 1 : -1));

        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/scenario-01.json")){
            this.instructions = objectMapper.readValue(is, Instruction[].class);
        } catch(IOException e){
            LOGGER.severe("cannot read instructions, " + e.getMessage());
        }

        for (final BaseEvent event: events){
            LOGGER.info(String.format("Event: %s", event));
        }
    }

    public void reset(){
        LOGGER.info("Resetting simulator");
        this.index = 0;
    }

    public void step(){
        if (index < events.size()){
            emitter.fire(events.get(this.index));
            this.index++;

        }
    }

    public void onBankEvent(@Observes BankEvent event) {
        LOGGER.info(String.format("Bank event: %s", event));
        final Bank bank = facade.findBank();
        final Account mirror = facade.findAccount(bank, Account.MIRROR_NAME);
        List<Transfer> transfers = Collections.emptyList();
        switch(event.getName()){
            case "opening":
                LOGGER.info(String.format("Bank opening: %s", mirror.getPosition()));
                accountManager.reset();
                break;
            case "sct":
                final List<Settlement> settlements = Arrays.stream(this.instructions)
                        .filter(i -> i instanceof Settlement && i.getWhen().isBefore(event.getWhen()))
                        .map(i -> (Settlement) i)
                        .collect(Collectors.toList());
                final List<Transfer> queue = settlementManager.buildSettlementQueue(settlements);
                LOGGER.info(String.format("Settlement queue size: %s", queue.size()));
                transfers = settlementManager.settleUnconditionally(queue);
                break;
            case "closing":
                LOGGER.info(String.format("Bank closing: %s", mirror.getPosition()));
                break;
        }
        accountManager.book(event.getWhen(), transfers);
    }

    public void onCurrencyEvent(@Observes CurrencyEvent event) {
        LOGGER.info(String.format("Currency event: %s", event));
        final String iso = event.getCurrency().getIso();
        final Bank bank = facade.findBank();
        List<Transfer> transfers = Collections.emptyList();
        switch(event.getName()){
            case "opening":
                break;
            case "fct":
                final List<PayIn> payIns = Arrays.stream(this.instructions)
                        .filter(i -> i instanceof PayIn && i.getWhen().isBefore(event.getWhen()))
                        .map(i -> (PayIn) i)
                        .collect(Collectors.toList());
                transfers = payInManager.bookPayIns(payIns, iso);
                break;
            case "close":
                transfers = payOutManager.computePayOuts(iso);
                break;
            case "closing":
                break;
        }
        accountManager.book(event.getWhen(), transfers);
    }

}
