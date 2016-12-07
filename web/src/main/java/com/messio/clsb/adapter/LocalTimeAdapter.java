package com.messio.clsb.adapter;

import com.messio.clsb.util.Converter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

/**
 * Created by jpc on 9/26/16.
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    public static final Converter<LocalTime, String> CONVERTER = new Converter<LocalTime, String>() {
        @Override
        public String marshal(LocalTime localTime) {
            return localTime == null ? null : localTime.toString();
        }

        @Override
        public LocalTime unmarshal(String s) {
            return s == null ? null : LocalTime.parse(s);
        }
    };

    @Override
    public LocalTime unmarshal(String v) throws Exception {
        return CONVERTER.unmarshal(v);
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return CONVERTER.marshal(v);
    }
}
