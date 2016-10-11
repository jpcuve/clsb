package com.messio.clsb.event;

import com.messio.clsb.entity.Bank;

import java.time.LocalTime;

/**
 * Created by jpc on 10-10-16.
 */
public class BankEvent extends BaseEvent {
    private Bank bank;

    public BankEvent(LocalTime when, String name, Bank bank) {
        super(when, name);
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    @Override
    public String toString() {
        return String.format("+ %s %s", getWhen(), getName());
    }
}
