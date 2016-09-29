package com.messio.clsb.entity;

import com.messio.clsb.Position;
import com.messio.clsb.adapter.LocalTimeAdapter;
import com.messio.clsb.adapter.PositionAdapter;
import com.messio.clsb.util.FieldConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Created by jpc on 22-09-16.
 */
@Entity
@Table(name = "movements")
public class Movement {
    private static final FieldConverter<String, Position> CONVERTER_POSITION = new FieldConverter<>(new PositionAdapter());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "orig_account_id")
    private Account orig;
    @ManyToOne
    @JoinColumn(name = "dest_account_id")
    private Account dest;
    @Basic
    @Column(name = "amount")
    private String amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getOrig() {
        return orig;
    }

    public void setOrig(Account orig) {
        this.orig = orig;
    }

    public Account getDest() {
        return dest;
    }

    public void setDest(Account dest) {
        this.dest = dest;
    }

    public Position getAmount() {
        return CONVERTER_POSITION.unmarshal(amount);
    }

    public void setAmount(Position amount) {
        this.amount = CONVERTER_POSITION.marshal(amount);
    }
}
