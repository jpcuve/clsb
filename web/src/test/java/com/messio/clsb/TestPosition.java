package com.messio.clsb;

import org.junit.Test;

/**
 * Created by jpc on 29-09-16.
 */
public class TestPosition {
    @Test
    public void testAddition(){
        final Position p1 = new Position("EUR", 10, "USD", 5);
        final Position p2 = new Position("USD", 15, "JPY", 100);
        System.out.println(p1.add(p2));
        System.out.println(p2.add(p1));
        System.out.println(p2.negate());
    }

    @Test
    public void testShortAndLong(){
        final Position p1 = new Position("EUR", 10, "USD", -5, "JPY", 100);
        System.out.println(p1.xshort());
        System.out.println(p1.xlong());
    }

    @Test
    public void testNegate(){
        final Position p1 = new Position("EUR", 10, "USD", -5, "JPY", 100);
        System.out.println(p1.negate());

    }
}
