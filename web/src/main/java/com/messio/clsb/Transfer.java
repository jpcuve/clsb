package com.messio.clsb;


/**
 * Created by jpc on 9/22/16.
 */
public class Transfer {
    public static final String MIRROR_NAME = "_MIRROR_";
    private String orig;
    private Position amount;
    private String dest;

    public Transfer(String orig, Position amount, String dest) {
        this.orig = orig;
        this.amount = amount;
        this.dest = dest;
    }

    // pay-out
    public Transfer(String orig, Position amount){
        this(orig, amount, MIRROR_NAME);
    }

    // pay-in
    public Transfer(Position amount, String dest){
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

    public Position getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s -> %s", orig, amount, dest);
    }
}
