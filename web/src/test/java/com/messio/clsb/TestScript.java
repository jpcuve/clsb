package com.messio.clsb;

import com.messio.clsb.util.script.Environment;
import com.messio.clsb.util.script.Parser;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

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
                "simple()",
                "sin(abc())",
                "start(2,3,'coucou',@)"
        };
        for (String test: tests){
            System.out.println(Parser.toString(new Parser(test).parse()));
        }
    }

    @Test
    public void testEnvironment() throws ParseException {
        final Environment e = new Environment() {
            @Override
            public Object call(String function, List<Object> arguments) {
                System.out.printf("fn: %s, args: %s%n", function, arguments.stream().map(Parser::toString).collect(Collectors.joining(",")));
                return null;
            }
        };
        e.eval("sin(3,cos(2,4))");
    }
}
