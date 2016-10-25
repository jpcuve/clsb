package com.messio.clsb.util.script;

import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * Created by jpc on 9/7/15.
 */
public class TokenIterator implements Iterator<Token> {
    private final String input;
    private int start;
    private Token token;

    public TokenIterator(String input) {
        this.input = input;
        this.start = 0;
        advance();
    }

    private void advance(){
        this.token = null;
        final String s = input.substring(start);
        for (final TokenInfo token: TokenInfo.values()){
            final Matcher matcher = token.getPattern().matcher(s);
            if (matcher.find()){
                final String group = matcher.group();
                this.token = new Token(token, group.trim());
                start += group.length();
                break;
            }
        }
    }

    public boolean hasNext() {
        return token != null;
    }

    public boolean isError(){
        return !hasNext() && start != input.length();
    }

    public Token next() {
        final Token t = this.token;
        advance();
        return t;
    }

}
