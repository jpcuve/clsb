package com.messio.clsb.adapter;

import com.messio.clsb.entity.LocalTimeConverter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

/**
 * Created by jpc on 9/26/16.
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    public static final LocalTimeConverter CONVERTER = new LocalTimeConverter();

    @Override
    public LocalTime unmarshal(String v) throws Exception {
        return CONVERTER.convertToEntityAttribute(v);
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return CONVERTER.convertToDatabaseColumn(v);
    }
}
