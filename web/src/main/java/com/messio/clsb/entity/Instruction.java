package com.messio.clsb.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.messio.clsb.adapter.LocalTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;

/**
 * Created by jpc on 9/26/16.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public class Instruction {
    private LocalTime when;
    private String account;

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

    @Override
    public String toString() {
        return String.format("%s:%s", when, account);
    }
}
