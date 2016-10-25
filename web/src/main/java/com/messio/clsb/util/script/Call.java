package com.messio.clsb.util.script;

import java.util.List;

/**
 * Created by jpc on 24-10-16.
 */
public class Call {
    final String function;
    final List<?> arguments;

    public Call(String function, List<?> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public String getFunction() {
        return function;
    }

    public List<?> getArguments() {
        return arguments;
    }

}
