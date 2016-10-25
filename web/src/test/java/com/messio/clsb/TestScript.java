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
                "-10",
                "+15",
                "12.3",
                "10.",
                ".1",
                ".3e10",
                "1.2e-12",
                "-1.41e+12",
                "sin('abc',2)",
                " sin(3, cos(2, 4))",
                "simple()",
                "sin(abc())",
                "start(2,3,'coucou',@)"
        };
        for (String test: tests){
            Object parse = new Parser(test).parse();
            System.out.printf("%s [%s]%n", Parser.toString(parse), parse.getClass().getName());
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
