package com.messio.clsb.entity;

import com.messio.clsb.Position;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jpc on 22-09-16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Account.ACCOUNT_ALL, query = "select a from Account a order by a.name"),
        @NamedQuery(name = Account.ACCOUNT_BY_NAME, query = "select a from Account a where a.name = :name")
})
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Account {
    public static final String ACCOUNT_BY_NAME = "Account.byName";
    public static final String ACCOUNT_ALL = "account.all";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Column(name = "position")
    private Position position;

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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
