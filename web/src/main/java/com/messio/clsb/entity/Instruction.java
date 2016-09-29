package com.messio.clsb.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.messio.clsb.adapter.LocalTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Created by jpc on 9/26/16.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public class Instruction {
    private LocalTime when;
    private String account;
    private BigDecimal amount;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getWhen() {
        return when;
    }

    public void setWhen(LocalTime when) {
        this.when = when;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("[%s]%s:%s:%s", getClass().getSimpleName(), when, account, amount);
    }
}
