package com.messio.clsb.entity;

import com.messio.clsb.Position;
import com.messio.clsb.adapter.PositionAdapter;
import com.messio.clsb.util.FieldConverter;

import javax.persistence.*;

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
    @Column(name = "information")
    private String information;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Position getAmount() {
        return CONVERTER_POSITION.unmarshal(amount);
    }

    public void setAmount(Position amount) {
        this.amount = CONVERTER_POSITION.marshal(amount);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s -> %s", information, orig.getName(), amount, dest.getName());
    }
}
