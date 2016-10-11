package com.messio.clsb;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jpc on 9/29/16.
 */
public class Position extends HashMap<String, BigDecimal> {
    public static final Position ZERO = new Position();

    public Position() {
    }

    public Position(String iso, double amount) {
        this.putAmount(iso, amount);
    }

    public Position(String iso1, double amount1, String iso2, double amount2){
        this.putAmount(iso1, amount1).putAmount(iso2, amount2);
    }

    public Position(String iso1, double amount1, String iso2, double amount2, String iso3, double amount3){
        this.putAmount(iso1, amount1).putAmount(iso2, amount2).putAmount(iso3, amount3);
    }

    private Position putAmount(String iso, double amount){
        this.put(iso, new BigDecimal(amount));
        return this;
    }

    public boolean isLong(){
        for (BigDecimal amount: normalize().values()){
            if (amount.signum() < 0){
                return false;
            }
        }
        return !isZero();
    }

    public boolean isShort(){
        for (BigDecimal amount: normalize().values()){
            if (amount.signum() > 0){
                return false;
            }
        }
        return !isZero();
    }

    public Position xlong(){
        final Position ret = new Position();
        entrySet().stream().filter(e -> e.getValue().signum() > 0).forEach(e -> ret.put(e.getKey(), e.getValue()));
        return ret;
    }

    public Position xshort(){
        final Position ret = new Position();
        entrySet().stream().filter(e -> e.getValue().signum() < 0).forEach(e -> ret.put(e.getKey(), e.getValue()));
        return ret;
    }

    public Position normalize(){
        final Position ret = new Position();
        entrySet().stream().filter(e -> e.getValue().signum() != 0).forEach(e -> ret.put(e.getKey(), e.getValue()));
        return ret;
    }

    public boolean isZero(){
        return normalize().isEmpty();
    }

    public Position add(Position that){
        final Position ret = new Position();
        ret.putAll(that);
        entrySet().forEach(e -> ret.put(e.getKey(), e.getValue().add(ret.getOrDefault(e.getKey(),BigDecimal.ZERO))));
        return ret;
    }

    public Position subtract(Position that){
        return add(that.negate());
    }

    public Position negate(){
        final Position ret = new Position();
        entrySet().forEach(e -> ret.put(e.getKey(), e.getValue().negate()));
        return ret;
    }

    public Position filter(String... isos){
        final Position ret = new Position();
        final List<String> ts = Arrays.asList(isos);
        entrySet().stream().filter(e -> ts.contains(e.getKey())).forEach(e -> ret.put(e.getKey(), e.getValue()));
        return ret;
    }

    @Override
    public String toString() {
        return entrySet().stream().map(e -> String.format("%s:%s", e.getKey(), e.getValue())).collect(Collectors.joining(";"));
    }
}
