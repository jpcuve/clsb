package com.messio.clsb;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jpc on 29-09-16.
 */
public class TestPosition {
    @Test
    public void testAdditionAndNegate(){
        final Position p1 = new Position("EUR", 10, "USD", 5);
        final Position p2 = new Position("USD", 15, "JPY", 100);
        assertEquals(new Position("EUR", 10, "USD", 20, "JPY", 100), p1.add(p2));
        assertEquals(new Position("EUR", 10, "USD", 20, "JPY", 100), p2.add(p1));
        assertEquals(new Position("USD", -15, "JPY", -100), p2.negate());
    }

    @Test
    public void testShortAndLong(){
        final Position p1 = new Position("EUR", 10, "USD", -5, "JPY", 100);
        assertEquals(new Position("USD", -5), p1.xshort());
        assertTrue(p1.xshort().isShort());
        assertFalse(p1.xshort().isLong());
        assertEquals(new Position("EUR", 10, "JPY", 100), p1.xlong());
        assertTrue(p1.xlong().isLong());
        assertFalse(p1.xlong().isShort());
    }

    @Test
    public void applyVolatility(){
        final Position p1 = Position.parse("EUR:10;USD:-5;JPY:100");
        final Position vm = Position.parse("EUR:0.1;USD:0.2;JPY:0.01");
        assertEquals(Position.parse("EUR:9;USD:-6;JPY:99"), p1.applyVolatility(vm));

    }

}
