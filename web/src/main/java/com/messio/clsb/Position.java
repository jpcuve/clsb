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

    public Position add(Position that){
        final Set<String> currencies = new HashSet<>(keySet());
        currencies.addAll(that.keySet());
        final Position ret = new Position();
        currencies.forEach(c -> ret.put(c, getOrDefault(c, BigDecimal.ZERO).add(that.getOrDefault(c, BigDecimal.ZERO))));
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
        return entrySet().stream().map(e -> String.format("%s:%s", e.getKey(), e.getValue())).collect(Collectors.joining(","));
    }
}
