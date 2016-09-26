package com.messio.clsb;

import java.math.BigDecimal;

/**
 * Created by jpc on 9/22/16.
 */
public enum Currency {
    EUR,
    USD,
    JPY;

    public static void main(String[] args) {
        final BigDecimal[] test = Positions.builder().leg(Currency.EUR, 100).leg(Currency.JPY, -30000).build();
        System.out.println(Positions.toString(test));
    }

}
