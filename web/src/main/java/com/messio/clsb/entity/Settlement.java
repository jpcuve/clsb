package com.messio.clsb.entity;

import java.math.BigDecimal;

/**
 * Created by jpc on 9/26/16.
 */
public class Settlement extends Instruction {
    private String counterparty;
    private BigDecimal amount;

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
