package com.messio.clsb.event;

import com.messio.clsb.entity.Currency;

import java.time.LocalTime;

/**
 * Created by jpc on 10-10-16.
 */
public class CurrencyEvent extends BaseEvent {
    private final Currency currency;

    public CurrencyEvent(LocalTime when, String name, Currency currency) {
        super(when, name);
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return String.format("+ %s %s %s", getWhen(), getName(), currency.getIso());
    }
}
