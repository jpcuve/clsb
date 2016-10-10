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
        @NamedQuery(name = Currency.CURRENCY_ALL, query = "select c from Currency c order by c.iso"),
        @NamedQuery(name = Currency.CURRENCY_BY_BANK, query = "select c from Currency c where c.bank = :bank order by c.iso"),
        @NamedQuery(name = Currency.CURRENCY_BY_ISO_BY_BANK, query = "select c from Currency c where c.iso = :iso and c.bank = :bank")
})
@Table(name = "currencies", uniqueConstraints = {@UniqueConstraint(columnNames = {"iso", "bank_id"})})
public class Currency {
    public static final String CURRENCY_ALL = "currency.all";
    public static final String CURRENCY_BY_ISO_BY_BANK = "currency.byIsoByBank";
    public static final String CURRENCY_BY_BANK = "currency.byBank";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "iso")
    private String iso;
    @Basic
    @Column(name = "opening")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime opening;
    @Basic
    @Column(name = "closing")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime closing;
    @Basic
    @Column(name = "funding_completion_target")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime fundingCompletionTarget;
    @Basic
    @Column(name = "close")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime close;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
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

    public LocalTime getFundingCompletionTarget() {
        return fundingCompletionTarget;
    }

    public void setFundingCompletionTarget(LocalTime fundingCompletionTarget) {
        this.fundingCompletionTarget = fundingCompletionTarget;
    }

    public LocalTime getClose() {
        return close;
    }

    public void setClose(LocalTime close) {
        this.close = close;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
