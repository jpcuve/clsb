package com.messio.clsb.entity;

import com.messio.clsb.Position;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Created by jpc on 01-10-16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Bank.BANK_BY_NAME, query = "select b from Bank b where b.name = :name"),
        @NamedQuery(name = Bank.BANK_ALL, query = "select b from Bank b")
})
@Table(name = "banks", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Bank {
    public static final String DEFAULT_NAME = "clsb";
    public static final String BANK_BY_NAME = "bank.byName";
    public static final String BANK_ALL = "bank.all";
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "name", nullable = false, unique = true)
    private String name = DEFAULT_NAME;
    @Basic
    @Column(name = "opening", nullable = false)
    private LocalTime opening = LocalTime.of(8, 0);
    @Basic
    @Column(name = "closing", nullable = false)
    private LocalTime closing = LocalTime.of(17, 0);
    @Basic
    @Column(name = "sct", nullable = false)
    private LocalTime settlementCompletionTarget = LocalTime.of(10, 0);
    @Basic
    @Column(name = "minimum_pay_in", nullable = false)
    private String minimumPayIn;

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

    public Position getMinimumPayIn() {
        return Position.CONVERTER.unmarshal(minimumPayIn);
    }

    public void setMinimumPayIn(Position minimumPayIn) {
        this.minimumPayIn = Position.CONVERTER.marshal(minimumPayIn);
    }
}
