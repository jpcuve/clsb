package com.messio.clsb;

import java.math.BigDecimal;
import java.util.EnumMap;

/**
 * Created by jpc on 9/22/16.
 */
public class Positions {
    public static class Builder {
        private EnumMap<Currency, BigDecimal> map = new EnumMap<>(Currency.class);

        public Builder leg(Currency ccy, double amount){
            map.put(ccy, new BigDecimal(amount));
            return this;
        }

        public BigDecimal[] build(){
            final BigDecimal[] ret = new BigDecimal[Currency.values().length];
            int i = 0;
            for (Currency ccy: Currency.values()){
                ret[i++] = map.getOrDefault(ccy, BigDecimal.ZERO);
            }
            return ret;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public static String toString(BigDecimal[] legs){
        final StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < Currency.values().length; i++){
            sb.append(String.format("%s %s|", Currency.values()[i], i < legs.length ? legs[i] : 0));
        }
        return sb.toString();
    }


    public static BigDecimal[] neg(BigDecimal[] legs){
        final BigDecimal[] ret = new BigDecimal[legs.length];
        for (int i = 0; i < ret.length; i++){
            ret[i] = legs[i] == null ? BigDecimal.ZERO : legs[i].negate();
        }
        return ret;
    }

    public static BigDecimal[] add(BigDecimal[] legs1, BigDecimal[] legs2){
        int l = Math.min(legs1.length, legs2.length);
        final BigDecimal[] ret = new BigDecimal[l];
        for (int i = 0; i < l; i++){
            ret[i] = (legs1[i] == null ? BigDecimal.ZERO : legs1[i]).add(legs2[i] == null ? BigDecimal.ZERO : legs2[i]);
        }
        return ret;
    }
}
