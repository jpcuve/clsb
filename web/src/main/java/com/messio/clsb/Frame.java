package com.messio.clsb;

import com.messio.clsb.entity.Instruction;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by jpc on 22-09-16.
 */
public class Frame {
    private final LocalTime from;
    private final LocalTime to;
    private final List<Instruction> instructions;

    public Frame(LocalTime from, LocalTime to, List<Instruction> instructions) {
        this.from = from;
        this.to = to;
        this.instructions = instructions;
    }

    public LocalTime getFrom() {
        return from;
    }

    public LocalTime getTo() {
        return to;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}

