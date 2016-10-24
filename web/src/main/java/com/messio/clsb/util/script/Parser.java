package com.messio.clsb.util.script;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jpc on 24-10-16.
 */
public class Parser {
    private final TokenIterator it;
    private Token lookAhead;

    public Parser(String text) {
        this.it = new TokenIterator(text);
        this.lookAhead = it.next();
    }

    public void accept(TokenInfo tokenInfo) throws ParseException {
        if (lookAhead.getTokenInfo() != tokenInfo){
            throw new ParseException(String.format("Unexpected token: %s", lookAhead), 0);
        }
        this.lookAhead = it.next();
    }

    public Object parse() throws ParseException {
        while (lookAhead.getTokenInfo() == TokenInfo.WHITESPACE){
            accept(TokenInfo.WHITESPACE);
        }
        switch(lookAhead.getTokenInfo()){
            case SYMBOL:
                final String function = lookAhead.getSequence();
                accept(TokenInfo.SYMBOL);
                accept(TokenInfo.PARENTHESIS_OPEN);
                final List<Object> arguments = new ArrayList<>();
                while (lookAhead.getTokenInfo() != TokenInfo.PARENTHESIS_CLOSE){
                    arguments.add(parse());
                    if (lookAhead.getTokenInfo() != TokenInfo.PARENTHESIS_CLOSE){
                        accept(TokenInfo.COMMA);
                    }
                }
                accept(TokenInfo.PARENTHESIS_CLOSE);
                return new Call(function, arguments);
            case STRING:
                final String s = lookAhead.getSequence().substring(1, lookAhead.getSequence().length() - 1);
                accept(TokenInfo.STRING);
                return s;
            case NUMBER_DECIMAL_INTEGER:
                final BigInteger bi = new BigInteger(lookAhead.getSequence());
                accept(TokenInfo.NUMBER_DECIMAL_INTEGER);
                return bi;
            default:
                throw new ParseException(String.format("Unexpected end of expression: %s", lookAhead.getSequence()), 0);
        }
    }

    public static String toString(Object o){
        if (o instanceof Call){
            final Call call = (Call) o;
            return String.format("%s(%s)", call.getFunction(), call.getArguments().stream().map(Parser::toString).collect(Collectors.joining(",")));
        } else if (o instanceof String){
            return String.format("'%s'", (String) o);
        } else {
            return o.toString();
        }
    }
}
