package com.messio.clsb;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jpc on 9/29/16.
 */
public class Position extends HashMap<String, BigDecimal> {
    public static final Position ZERO = new Position();

    public Position() {
    }

    public Position(String ccy, double amount) {
        this.put(ccy, new BigDecimal(amount));
    }

    public Position(String ccy1, double amount1, String ccy2, double amount2){
        this.put(ccy1, new BigDecimal(amount1));
        this.put(ccy2, new BigDecimal(amount2));
    }

    public Position(String ccy1, double amount1, String ccy2, double amount2, String ccy3, double amount3){
        this.put(ccy1, new BigDecimal(amount1));
        this.put(ccy2, new BigDecimal(amount2));
        this.put(ccy3, new BigDecimal(amount3));
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

    @Override
    public String toString() {
        return entrySet().stream().map(e -> String.format("%s:%s", e.getKey(), e.getValue())).collect(Collectors.joining(";"));
    }
}
