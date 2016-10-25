package com.messio.clsb.util.script;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jpc on 10/25/16.
 */
public abstract class Environment {
    public abstract Object call(String function, List<Object> arguments);

    public Object eval(String expression) throws ParseException {
        return eval(new Parser(expression).parse());
    }

    private Object eval(Object o){
        if (o instanceof Call){
            final Call call = (Call) o;
            return call(call.getFunction(), call.getArguments().stream().map(this::eval).collect(Collectors.toList()));
        }
        return o;
    }
}
