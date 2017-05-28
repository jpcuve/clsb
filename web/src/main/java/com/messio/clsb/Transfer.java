package com.messio.clsb;


import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.PayIn;
import com.messio.clsb.entity.PayOut;
import com.messio.clsb.entity.Settlement;

/**
 * Created by jpc on 9/22/16.
 */
public class Transfer {
    private final String information;
    private final String orig;
    private final Position amount;
    private final String dest;

    private Transfer(String information, String orig, Position amount, String dest) {
        this.information = information;
        this.orig = orig;
        this.amount = amount;
        this.dest = dest;
    }

    public Transfer(PayIn payIn){
        this(payIn.toString(), Account.MIRROR_NAME, payIn.getAmount(), payIn.getAccount());
    }

    public Transfer(PayOut payOut){
        this(payOut.toString(), payOut.getAccount(), payOut.getAmount(), Account.MIRROR_NAME);
    }

    public Transfer(Settlement db, Settlement cr){
        this(String.format("%s,%s", db, cr), db.getAccount(), db.getAmount(), cr.getAccount());
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
