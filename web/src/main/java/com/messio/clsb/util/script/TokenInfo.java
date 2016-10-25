package com.messio.clsb.util.script;

import java.util.regex.Pattern;

/**
 * Created by jpc on 9/7/15.
 */
public enum TokenInfo {
    WHITESPACE("\\s+"),
    NULL("@"),
    COMMA(","),
    PARENTHESIS_OPEN("\\("),
    PARENTHESIS_CLOSE("\\)"),
    STRING("\\'.+\\'"),
    FLOAT("[-+]?[0-9]*\\.[0-9]+([eE][-+]?[0-9]+)?"),
    INTEGER("[-+]?[0-9]+"),
    SYMBOL("[a-zA-Z][a-zA-Z0-9_]*"),
    INVALID(null)
    ;

    private Pattern pattern;

    TokenInfo(String regex) {
        this.pattern = Pattern.compile("^" + regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
