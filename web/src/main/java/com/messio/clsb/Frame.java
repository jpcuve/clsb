package com.messio.clsb;

import java.time.LocalTime;

/**
 * Created by jpc on 22-09-16.
 */
public class Frame {
    private final LocalTime from;
    private final LocalTime to;

    public Frame(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalTime getFrom() {
        return from;
    }

    public LocalTime getTo() {
        return to;
    }
}
