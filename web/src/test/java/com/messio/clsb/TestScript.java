package com.messio.clsb;

import com.messio.clsb.util.script.Parser;
import org.junit.Test;

import java.text.ParseException;

/**
 * Created by jpc on 24-10-16.
 */
public class TestScript {
    @Test
    public void testParser() throws ParseException {
        final String[] tests = {
                "'abc'",
                "1234",
                "sin('abc',2)",
                " sin(3, cos(2, 4))",
                "simple()"
        };
        for (String test: tests){
            System.out.println(Parser.toString(new Parser(test).parse()));
        }
    }
}
