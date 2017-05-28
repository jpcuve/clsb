package com.messio.clsb.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.messio.clsb.Position;
import java.time.LocalTime;

/**
 * Created by jpc on 9/26/16.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public class Instruction {
    private LocalTime when;
    private LocalTime transferred = null;
    private String account;
    private String reference;
    private Position amount;

    public LocalTime getWhen() {
        return when;
    }

    public void setWhen(LocalTime when) {
        this.when = when;
    }

    public LocalTime getTransferred() {
        return transferred;
    }

    public void setTransferred(LocalTime transferred) {
        this.transferred = transferred;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Position getAmount() {
        return amount;
    }

    public void setAmount(Position amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%s;%s[%s](%s)%s", when, account, getClass().getSimpleName(), reference, amount);
    }
}
