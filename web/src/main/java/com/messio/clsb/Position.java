package com.messio.clsb;

import com.messio.clsb.entity.PositionConverter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jpc on 9/29/16.
 */
public class Position extends HashMap<String, BigDecimal> {
    public static final Position ZERO = new Position();
    public static final PositionConverter CONVERTER = new PositionConverter();

    public static Position parse(String s) {
        return CONVERTER.convertToEntityAttribute(s);
    }

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

    public Position(Position p){
        this.putAll(p);
    }

    private Position putAmount(String iso, double amount){
        this.put(iso, new BigDecimal(amount));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position){
            final Position that = (Position) o;
            return subtract(that).normalize().isZero();
        }
        return false;
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

    public Position applyVolatility(Position that){
        return entrySet().stream().collect(Position::new, (p, e) -> {
            final BigDecimal vm = that.getOrDefault(e.getKey(), BigDecimal.ZERO);
            final BigDecimal am = e.getValue();
            p.put(e.getKey(), am.signum() > 0 ? am.subtract(am.multiply(vm)): am.add(am.multiply(vm)));
        }, Position::add);
    }

    public Position one(){
        return entrySet().stream().collect(Position::new, (p, e) -> p.put(e.getKey(), BigDecimal.ONE), Position::add);
    }

    public Position xlong(){
        return entrySet().stream().filter(e -> e.getValue().signum() > 0).collect(Position::new, (p, e) -> p.put(e.getKey(), e.getValue()), Position::add);
    }

    public Position xshort(){
        return entrySet().stream().filter(e -> e.getValue().signum() < 0).collect(Position::new, (p, e) -> p.put(e.getKey(), e.getValue()), Position::add);
    }

    public Position normalize(){
        return entrySet().stream().filter(e -> e.getValue().signum() != 0).collect(Position::new, (p, e) -> p.put(e.getKey(), e.getValue()), Position::add);
    }

    public boolean isZero(){
        return normalize().isEmpty();
    }

    public Position add(Position that){
        return entrySet().stream().collect(() -> new Position(that), (p, e) -> p.put(e.getKey(), e.getValue().add(p.getOrDefault(e.getKey(),BigDecimal.ZERO))), Position::add);
    }

    public Position subtract(Position that){
        return add(that.negate());
    }

    public Position negate(){
        return entrySet().stream().collect(Position::new, (p, e) -> p.put(e.getKey(), e.getValue().negate()), Position::add);
    }

    public Position filter(String... isos){
        final List<String> ts = Arrays.asList(isos);
        return entrySet().stream().filter(e -> ts.contains(e.getKey())).collect(Position::new, (p, e) -> p.put(e.getKey(), e.getValue()), Position::add);
    }

    @Override
    public String toString() {
        return CONVERTER.convertToDatabaseColumn(this);
    }
}
