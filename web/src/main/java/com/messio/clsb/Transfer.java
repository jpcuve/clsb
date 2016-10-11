package com.messio.clsb;


import com.messio.clsb.entity.Account;

/**
 * Created by jpc on 9/22/16.
 */
public class Transfer {
    private final String information;
    private final String orig;
    private final Position amount;
    private final String dest;

    public Transfer(String information, String orig, Position amount, String dest) {
        this.information = information;
        this.orig = orig;
        this.amount = amount;
        this.dest = dest;
    }

    // pay-out
    public Transfer(String information, String orig, Position amount){
        this(information, orig, amount, Account.MIRROR_NAME);
    }

    // pay-in
    public Transfer(String information, Position amount, String dest){
        this(information, Account.MIRROR_NAME, amount, dest);
    }

    public boolean isPayIn(){
        return Account.MIRROR_NAME.equals(orig);
    }

    public boolean isPayOut(){
        return Account.MIRROR_NAME.equals(dest);
    }

    public String getInformation() {
        return information;
    }

    public String getOrig() {
        return orig;
    }

    public String getDest() {
        return dest;
    }

    public Position getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s -> %s", information, orig, amount, dest);
    }
}
