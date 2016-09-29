package com.messio.clsb.adapter;

import com.messio.clsb.Position;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jpc on 9/29/16.
 */
public class PositionAdapter extends XmlAdapter<String, Position> {
    @Override
    public Position unmarshal(String v) throws Exception {
        final Position position = new Position();
        Arrays.stream(v.split(";")).filter(l -> l.contains(":")).forEach(l -> position.put(l.substring(0, l.indexOf(":")), new BigDecimal(l.substring(l.indexOf(":") + 1))));
        return position;
    }

    @Override
    public String marshal(Position v) throws Exception {
        return v.entrySet().stream().map(e -> String.format("%s:%s", e.getKey(), e.getValue())).collect(Collectors.joining(";"));
    }
}
