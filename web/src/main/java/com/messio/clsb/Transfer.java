package com.messio.clsb;

import java.math.BigDecimal;

/**
 * Created by jpc on 9/22/16.
 */
public class Transfer {
    public static final String MIRROR_NAME = "_MIRROR_";
    private String orig;
    private BigDecimal amount;
    private String dest;

    public Transfer(String orig, BigDecimal amount, String dest) {
        this.orig = orig;
        this.amount = amount;
        this.dest = dest;
    }

    // pay-out
    public Transfer(String orig, BigDecimal amount){
        this(orig, amount, MIRROR_NAME);
    }

    // pay-in
    public Transfer(BigDecimal amount, String dest){
        this(MIRROR_NAME, amount, dest);
    }

    public boolean isPayIn(){
        return MIRROR_NAME.equals(orig);
    }

    public boolean isPayOut(){
        return MIRROR_NAME.equals(dest);
    }

    public String getOrig() {
        return orig;
    }

    public String getDest() {
        return dest;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s -> %s", orig, amount, dest);
    }
}
