package com.messio.clsb.entity;

import com.messio.clsb.Position;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jpc on 28-01-17.
 */
@Converter
public class PositionConverter implements AttributeConverter<Position, String> {
    @Override
    public String convertToDatabaseColumn(Position position) {
        return position == null ? null : position.entrySet().stream().map(e -> String.format("%s:%s", e.getKey(), e.getValue())).collect(Collectors.joining(";"));
    }

    @Override
    public Position convertToEntityAttribute(String s) {
        if (s != null){
            final Position position = new Position();
            Arrays.stream(s.split(";")).filter(l -> l.contains(":")).forEach(l -> position.put(l.substring(0, l.indexOf(":")), new BigDecimal(l.substring(l.indexOf(":") + 1))));
            return position;
        }
        return null;
    }
}
