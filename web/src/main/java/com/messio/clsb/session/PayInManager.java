package com.messio.clsb.session;

import com.messio.clsb.Frame;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Instruction;
import com.messio.clsb.entity.PayIn;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by jpc on 22-09-16.
 */
@Singleton(name = "clsb/pay-in-manager")
@LocalBean
public class PayInManager {
    public static final Logger LOGGER = Logger.getLogger(PayInManager.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;


    public void period(@Observes Frame frame) {
        for (PayIn payIn: frame.getPayIns()){
            LOGGER.info(String.format(" pay-in: %s", payIn));
            facade.book(Collections.singletonList(new Transfer(payIn.getAmount(), payIn.getAccount())));
        }
    }
}
