package com.messio.clsb.util;

/**
 * Created by jpc on 04-12-16.
 */
public interface Converter<E, F> {
    F marshal(E e);
    E unmarshal(F f);
}
