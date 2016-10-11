package com.messio.clsb.session;

import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.PayIn;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 22-09-16.
 */
@Singleton(name = "clsb/pay-in-manager")
@LocalBean
public class PayInManager {
    public static final Logger LOGGER = Logger.getLogger(PayInManager.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;

    public void bookPayIns(final Bank bank, final List<PayIn> payIns, final String iso){
        LOGGER.info(String.format("Booking pay-ins for %s", iso));
        final List<Transfer> transfers = payIns.stream()
                .filter(pi -> pi.getAmount().containsKey(iso))
                .map(pi -> new Transfer(String.format("pay-in: %s", pi.getReference()), pi.getAmount().filter(iso), pi.getAccount()))
                .collect(Collectors.toList());
        facade.book(bank, transfers);
    }
}
