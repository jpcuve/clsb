package com.messio.clsb.entity;

/**
 * Created by jpc on 9/26/16.
 */
public class Settlement extends Instruction {
    private String counterParty;

    public String getCounterParty() {
        return counterParty;
    }

    public void setCounterParty(String counterParty) {
        this.counterParty = counterParty;
    }
}
