package com.messio.clsb.entity;

import java.math.BigDecimal;

/**
 * Created by jpc on 9/26/16.
 */
public class PayIn extends Instruction {
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", super.toString(), amount) ;
    }
}
