package com.messio.clsb.session;

import com.messio.clsb.Frame;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.logging.Logger;

/**
 * Created by jpc on 22-09-16.
 */
@Singleton(name = "clsb/clock")
@LocalBean
@Startup
public class Clock {
    private final Logger LOGGER = Logger.getLogger(Clock.class.getCanonicalName());
    private LocalTime localTime = LocalTime.of(0, 0);
    @Resource
    private TimerService timerService;
    @Inject
    private Event<Frame> frameEvent;

    @PostConstruct
    public void init(){
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        final Timer timer = timerService.createIntervalTimer(0, 5000L, timerConfig);
    }

    @Timeout
    public void timeout(){
        frameEvent.fire(new Frame(localTime, localTime.plusMinutes(10)));
        localTime = localTime.plusMinutes(10);
    }
}
