package com.messio.clsb.session;

import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Movement;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

/**
 * Created by jpc on 25-09-16.
 */
@Stateless(name = "clsb/clsb-facade")
@LocalBean
public class ClsbFacade {
    @PersistenceContext
    private EntityManager em;

    public Account loadAccount(final String name){
        return em.createNamedQuery(Account.ACCOUNT_BY_NAME, Account.class).setParameter("name", name).getResultList().stream().findFirst().orElseGet(() -> {
            final Account a = new Account();
            a.setName(name);
            a.setPosition(BigDecimal.ZERO);
            em.persist(a);
            return a;
        });
    }

    public Movement book(Transfer transfer){
        final Account origAccount = loadAccount(transfer.getOrig());
        final Account destAccount = loadAccount(transfer.getDest());
        final BigDecimal amount = transfer.getAmount();
        origAccount.setPosition(origAccount.getPosition().subtract(amount));
        destAccount.setPosition(destAccount.getPosition().add(amount));
        final Movement movement = new Movement();
        movement.setOrig(origAccount);
        movement.setDest(destAccount);
        movement.setAmount(amount);
        em.persist(origAccount);
        em.persist(destAccount);
        em.persist(movement);
        return movement;
    }
}
