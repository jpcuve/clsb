package com.messio.clsb.event;

import java.time.LocalTime;

/**
 * Created by jpc on 10/10/16.
 */
public class BaseEvent {
    private final LocalTime when;
    private final String name;

    public BaseEvent(LocalTime when, String name) {
        this.when = when;
        this.name = name;
    }

    public LocalTime getWhen() {
        return when;
    }

    public String getName() {
        return name;
    }
}
