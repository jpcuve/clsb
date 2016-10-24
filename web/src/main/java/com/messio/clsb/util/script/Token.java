package com.messio.clsb.util.script;

/**
 * Created by jpc on 9/7/15.
 */
public class Token {
    private final TokenInfo tokenInfo;
    private final String sequence;

    public Token(TokenInfo tokenInfo, String sequence) {
        this.tokenInfo = tokenInfo;
        this.sequence = sequence;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public String getSequence() {
        return sequence;
    }

    @Override
    public String toString() {
        return String.format("Token{%s,%s}", tokenInfo, sequence);
    }
}
