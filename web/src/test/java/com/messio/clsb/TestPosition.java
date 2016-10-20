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
        assertEquals(p1.add(p2), new Position("EUR", 10, "USD", 20, "JPY", 100));
        assertEquals(p2.add(p1), new Position("EUR", 10, "USD", 20, "JPY", 100));
        assertEquals(p2.negate(), new Position("USD", -15, "JPY", -100));
    }

    @Test
    public void testShortAndLong(){
        final Position p1 = new Position("EUR", 10, "USD", -5, "JPY", 100);
        assertEquals(p1.xshort(), new Position("USD", -5));
        assertTrue(p1.xshort().isShort());
        assertFalse(p1.xshort().isLong());
        assertEquals(p1.xlong(),  new Position("EUR", 10, "JPY", 100));
        assertTrue(p1.xlong().isLong());
        assertFalse(p1.xlong().isShort());
    }

}
