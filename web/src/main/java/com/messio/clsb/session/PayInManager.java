package com.messio.clsb.session;

import com.messio.clsb.Frame;
import com.messio.clsb.entity.Instruction;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import java.util.logging.Logger;

/**
 * Created by jpc on 22-09-16.
 */
@Singleton(name = "clsb/pay-in-manager")
@LocalBean
public class PayInManager {
    public static final Logger LOGGER = Logger.getLogger(PayInManager.class.getCanonicalName());


    public void period(@Observes Frame frame) {
        LOGGER.info(String.format("period: %s %s", frame.getFrom(), frame.getTo()));
        for (Instruction instruction: frame.getInstructions()){
            LOGGER.info(String.format(" instruction: %s", instruction));
        }
    }
}
