package com.messio.clsb.entity;

import com.messio.clsb.Position;
import com.messio.clsb.adapter.LocalTimeAdapter;
import com.messio.clsb.adapter.PositionAdapter;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Created by jpc on 22-09-16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Movement.MOVEMENT_BY_ACCOUNT, query = "select m from Movement m where (m.orig = :account or m.dest = :account) order by m.when"),
        @NamedQuery(name = Movement.MOVEMENT_DELETE, query = "delete from Movement")
})
@Table(name = "movements")
public class Movement {
    public static final String MOVEMENT_BY_ACCOUNT = "movement.byAccount";
    public static final String MOVEMENT_DELETE = "movement.delete";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "when")
    private String when;
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

    public LocalTime getWhen() {
        return LocalTimeAdapter.CONVERTER.unmarshal(when);
    }

    public void setWhen(LocalTime when) {
        this.when = LocalTimeAdapter.CONVERTER.marshal(when);
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
        return PositionAdapter.CONVERTER.unmarshal(amount);
    }

    public void setAmount(Position amount) {
        this.amount = PositionAdapter.CONVERTER.marshal(amount);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s -> %s -> %s", information, when, orig.getName(), amount, dest.getName());
    }
}
