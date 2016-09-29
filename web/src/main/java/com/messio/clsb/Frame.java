package com.messio.clsb;

import com.messio.clsb.entity.Instruction;
import com.messio.clsb.entity.PayIn;
import com.messio.clsb.entity.Settlement;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<PayIn> getPayIns() {
        return instructions.stream().filter(i -> PayIn.class.equals(i.getClass())).map(i -> (PayIn) i).collect(Collectors.toList());
    }

    public List<Settlement> getSettlements() {
        return instructions.stream().filter(i -> Settlement.class.equals(i.getClass())).map(i -> (Settlement) i).collect(Collectors.toList());
    }


}

