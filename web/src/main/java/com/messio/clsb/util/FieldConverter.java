package com.messio.clsb.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by jpc on 29-09-16.
 */
public class FieldConverter<E, F> {
    private final XmlAdapter<E, F> adapter;

    public FieldConverter(XmlAdapter<E, F> adapter) {
        this.adapter = adapter;
    }

    public F unmarshal(E v) {
        if (v != null ) try {
            return adapter.unmarshal(v);
        } catch(Exception e){
            // ignore
        }
        return null;
    }

    public E marshal(F v) {
        if (v != null) try {
            return adapter.marshal(v);
        } catch(Exception e){
            // ignore
        }
        return null;
    }
}
