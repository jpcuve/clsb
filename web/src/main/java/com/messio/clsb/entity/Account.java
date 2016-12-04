package com.messio.clsb.entity;

import com.messio.clsb.Position;
import com.messio.clsb.adapter.PositionAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by jpc on 22-09-16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Account.ACCOUNT_ALL, query = "select a from Account a order by a.name"),
        @NamedQuery(name = Account.ACCOUNT_BY_BANK, query = "select a from Account a where a.bank = :bank order by a.name"),
        @NamedQuery(name = Account.ACCOUNT_BY_NAME_BY_BANK, query = "select a from Account a where a.name = :name and a.bank = :bank")
})
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "bank_id"})})
public class Account {
    public static final String MIRROR_NAME = "_MIRROR_";
    public static final String ACCOUNT_BY_NAME_BY_BANK = "Account.byName";
    public static final String ACCOUNT_ALL = "account.all";
    public static final String ACCOUNT_BY_BANK = "account.byBank";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "short_position_limit", nullable = false)
    private String shortPositionLimitAsString;
    @Basic
    @Column(name = "position", nullable = false)
    private String positionAsString;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getShortPositionLimit(){
        return PositionAdapter.CONVERTER.unmarshal(shortPositionLimitAsString);
    }

    public void setShortPositionLimit(Position position){
        this.shortPositionLimitAsString = PositionAdapter.CONVERTER.marshal(position);
    }

    public Position getPosition() {
        return PositionAdapter.CONVERTER.unmarshal(positionAsString);
    }

    public void setPosition(Position position) {
        this.positionAsString = PositionAdapter.CONVERTER.marshal(position);
    }

    @XmlTransient
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
