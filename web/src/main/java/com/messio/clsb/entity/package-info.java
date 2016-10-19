/**
 * Created by jpc on 19-10-16.
 */
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(value=LocalTimeAdapter.class, type=LocalTime.class),
        @XmlJavaTypeAdapter(value=PositionAdapter.class, type=Position.class),

})
package com.messio.clsb.entity;

import com.messio.clsb.Position;
import com.messio.clsb.adapter.LocalTimeAdapter;
import com.messio.clsb.adapter.PositionAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalTime;