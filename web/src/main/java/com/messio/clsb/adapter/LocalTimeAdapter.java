package com.messio.clsb.adapter;

import com.messio.clsb.util.FieldConverter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

/**
 * Created by jpc on 9/26/16.
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    public static final FieldConverter<String, LocalTime> CONVERTER = new FieldConverter<>(new LocalTimeAdapter());

    @Override
    public LocalTime unmarshal(String v) throws Exception {
        return LocalTime.parse(v);
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return v.toString();
    }
}
