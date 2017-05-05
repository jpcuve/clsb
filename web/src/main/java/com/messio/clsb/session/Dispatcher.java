package com.messio.clsb.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messio.clsb.Transfer;
import com.messio.clsb.adapter.LocalTimeAdapter;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Instruction;
import com.messio.clsb.entity.PayIn;
import com.messio.clsb.entity.Settlement;
import com.messio.clsb.event.BankEvent;
import com.messio.clsb.event.BaseEvent;
import com.messio.clsb.event.CurrencyEvent;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.transaction.Transactional;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 05-05-17.
 */
@Singleton(name = "clsb/dispatcher")
@LocalBean
@Transactional
public class Dispatcher {
    public static final Logger LOGGER = Logger.getLogger(Dispatcher.class.getCanonicalName());
    @Inject
    private AccountManager accountManager;
    @Inject
    private ClsbFacade facade;
    @Inject
    private PayInManager payInManager;
    @Inject
    private PayOutManager payOutManager;
    @Inject
    private SettlementManager settlementManager;

    private Instruction[] instructions;

    @PostConstruct
    public void init(){
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/scenario-01.json")){
            this.instructions = objectMapper.readValue(is, Instruction[].class);
        } catch(IOException e){
            LOGGER.severe("cannot read instructions, " + e.getMessage());
        }
    }

    public void onBaseEvent(@Observes BaseEvent event){
        switch(event.getName()){
            case "init":
                LOGGER.info("Resetting simulator");
                accountManager.reset();
                break;
            case "done":
                break;
        }
    }

    public void onBankEvent(@Observes BankEvent event) {
        final Account mirror = facade.findAccount(event.getBank(), Account.MIRROR_NAME);
        List<Transfer> transfers = Collections.emptyList();
        switch(event.getName()){
            case "opening":
                LOGGER.info(String.format("Bank opening: %s", mirror.getPosition()));
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
        final String iso = event.getCurrency().getIso();
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
