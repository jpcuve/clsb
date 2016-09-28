package com.messio.clsb.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.messio.clsb.Frame;
import com.messio.clsb.entity.Instruction;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private Instruction[] instructions;

    @PostConstruct
    public void init(){
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        final Timer timer = timerService.createIntervalTimer(0, 5000L, timerConfig);
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule annotationModule = new JaxbAnnotationModule();
        objectMapper.registerModule(annotationModule);
        final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/scenario-01.json");
        try{
            this.instructions = objectMapper.readValue(is, Instruction[].class);
        } catch(IOException e){
            LOGGER.severe("cannot read scenario, " + e.getMessage());
        }
    }

    @Timeout
    public void timeout(){
        final LocalTime to = localTime.plusMinutes(10);
        final List<Instruction> list = Arrays.stream(instructions).filter(i -> localTime.isBefore(i.getWhen()) && i.getWhen().isBefore(to)).collect(Collectors.toList());
        frameEvent.fire(new Frame(localTime, to, list));
        localTime = to;
    }
}
