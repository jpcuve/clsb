package com.messio.clsb.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jpc on 22-09-16.
 */
@NamedQueries({
        @NamedQuery(name = Account.ACCOUNT_BY_NAME, query = "select a from Account a where a.name = :name")
})
@Entity
public class Account {
    public static final String ACCOUNT_BY_NAME = "Account.byName";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Column(name = "position")
    private BigDecimal position;

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

    public BigDecimal getPosition() {
        return position;
    }

    public void setPosition(BigDecimal position) {
        this.position = position;
    }
}
