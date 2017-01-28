package com.messio.clsb.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalTime;

/**
 * Created by jpc on 28-01-17.
 */
@Converter
public class LocalTimeConverter implements AttributeConverter<LocalTime, String> {
    @Override
    public String convertToDatabaseColumn(LocalTime localTime) {
        return localTime == null ? null : localTime.toString();    }

    @Override
    public LocalTime convertToEntityAttribute(String s) {
        return s == null ? null : LocalTime.parse(s);
    }
}
