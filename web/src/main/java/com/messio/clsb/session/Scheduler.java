package com.messio.clsb.session;

import com.messio.clsb.Frame;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created by jpc on 10/3/16.
 */
public class Scheduler {
    public static final Logger LOGGER = Logger.getLogger(Scheduler.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;

    public void period(@Observes Frame frame) {

    }
}
