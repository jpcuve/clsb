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
    private AccountManager accountManager;
    private PayInManager payInManager;
    private PayOutManager payOutManager;
    private SettlementManager settlementManager;

    public Dispatcher() {
    }

    @Inject
    public Dispatcher(
            final AccountManager accountManager,
            final PayInManager payInManager,
            final PayOutManager payOutManager,
            final SettlementManager settlementManager
    ){
        this.accountManager = accountManager;
        this.payInManager = payInManager;
        this.payOutManager = payOutManager;
        this.settlementManager = settlementManager;
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
        List<Transfer> transfers = Collections.emptyList();
        switch(event.getName()){
            case "opening":
                break;
            case "sct":
                final List<Transfer> queue = settlementManager.buildSettlementQueue(event.getWhen());
                LOGGER.info(String.format("Settlement queue size: %s", queue.size()));
                transfers = settlementManager.settleUnconditionally(queue);
                break;
            case "closing":
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
                transfers = payInManager.bookPayIns(event.getWhen(), iso);
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
