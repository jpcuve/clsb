package com.messio.clsb.entity;

import com.messio.clsb.adapter.LocalTimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;

/**
 * Created by jpc on 01-10-16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Bank.BANK_BY_NAME, query = "select b from Bank b where b.name = :name")
})
@Table(name = "banks", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Bank {
    public static final String DEFAULT_NAME = "clsb";
    public static final String BANK_BY_NAME = "bank.byName";
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "name", nullable = false, unique = true)
    private String name = DEFAULT_NAME;
    @Basic
    @Column(name = "opening", nullable = false)
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime opening = LocalTime.of(8, 0);
    @Basic
    @Column(name = "closing", nullable = false)
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime closing = LocalTime.of(17, 0);
    @Basic
    @Column(name = "sct", nullable = false)
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime settlementCompletionTarget = LocalTime.of(10, 0);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getOpening() {
        return opening;
    }

    public void setOpening(LocalTime opening) {
        this.opening = opening;
    }

    public LocalTime getClosing() {
        return closing;
    }

    public void setClosing(LocalTime closing) {
        this.closing = closing;
    }

    public LocalTime getSettlementCompletionTarget() {
        return settlementCompletionTarget;
    }

    public void setSettlementCompletionTarget(LocalTime settlementCompletionTarget) {
        this.settlementCompletionTarget = settlementCompletionTarget;
    }
}
