package com.messio.clsb.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.messio.clsb.Frame;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.entity.Instruction;
import com.messio.clsb.event.BaseEvent;
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
import java.util.Arrays;
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

    private Bank bank;
    private LocalTime localTime = LocalTime.MIN;
    private List<BaseEvent> events = new ArrayList<>();

    @PostConstruct
    public void init(){
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule annotationModule = new JaxbAnnotationModule();
        objectMapper.registerModule(annotationModule);

        final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/bank-01.json");
        try{
            final BankModel bankModel = objectMapper.readValue(is, BankModel.class);
            this.bank = facade.build(bankModel);
        } catch(IOException e){
            LOGGER.severe("cannot read bank model, " + e.getMessage());
        }
        events.add(new BaseEvent(bank.getOpening(), String.format("Opening bank %s", bank.getName())));
        events.add(new BaseEvent(bank.getClosing(), String.format("Closing bank %s", bank.getName())));
        for (final Currency currency: facade.findCurrencies(bank)){
            events.add(new BaseEvent(currency.getOpening(), String.format("Opening %s", currency.getIso())));
            events.add(new BaseEvent(currency.getClosing(), String.format("Closing %s", currency.getIso())));
        }
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        final Timer timer = timerService.createIntervalTimer(0, 100L, timerConfig);
    }

    @Timeout
    public void timeout(){
        final LocalTime to = localTime.plusMinutes(10);
        LOGGER.info(String.format("Period: %s %s", localTime, to));
        events.stream().filter(e -> (localTime.isBefore(e.getWhen()) || localTime.equals(e.getWhen())) && e.getWhen().isBefore(to)).forEach(e -> emitter.fire(e));
        localTime = to;
    }

    public void period(@Observes BaseEvent event) {
        LOGGER.info(String.format("Event: %s", event));
    }

}
