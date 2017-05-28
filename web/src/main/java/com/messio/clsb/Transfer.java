package com.messio.clsb;


import com.messio.clsb.entity.*;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jpc on 9/22/16.
 */
public class Transfer {
    private final String orig;
    private final Position amount;
    private final String dest;
    private final Instruction[] sources;
    private String information;

    private Transfer(String orig, Position amount, String dest, Instruction... sources) {
        this.orig = orig;
        this.amount = amount;
        this.dest = dest;
        this.sources = sources;
    }

    public Transfer(PayIn payIn){
        this(Account.MIRROR_NAME, payIn.getAmount(), payIn.getAccount(), payIn);
    }

    public Transfer(PayOut payOut){
        this(payOut.getAccount(), payOut.getAmount(), Account.MIRROR_NAME, payOut);
    }

    public Transfer(Settlement db, Settlement cr){
        this(db.getAccount(), db.getAmount(), cr.getAccount(), db, cr);
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

    public Instruction[] getSources() {
        return sources;
    }

    public String getInformation(){
        if (information == null){
            information = Arrays.stream(sources).map(Instruction::getReference).collect(Collectors.joining(","));
        }
        return information;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s -> %s", getInformation(), orig, amount, dest);
    }
}
