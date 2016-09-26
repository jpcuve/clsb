package com.messio.clsb.entity;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Created by jpc on 22-09-16.
 */
public class Movement {
    private Long id;
    private LocalTime created;
    private Account orig;
    private Account dest;
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getCreated() {
        return created;
    }

    public void setCreated(LocalTime created) {
        this.created = created;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
