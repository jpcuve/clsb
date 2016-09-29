package com.messio.clsb.session;

import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Movement;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            a.setPosition(Position.ZERO);
            em.persist(a);
            return a;
        });
    }

    public List<Movement> book(List<Transfer> transfers){
        final List<Movement> list = new ArrayList<>();
        final Map<String, Account> accountMap = new HashMap<>();
        for (final Transfer transfer: transfers){
            final Account origAccount = accountMap.computeIfAbsent(transfer.getOrig(), k -> loadAccount(transfer.getOrig()));
            final Account destAccount = accountMap.computeIfAbsent(transfer.getDest(), k -> loadAccount(transfer.getDest()));
            final Position amount = transfer.getAmount();
            origAccount.setPosition(origAccount.getPosition().subtract(amount));
            destAccount.setPosition(destAccount.getPosition().add(amount));
            final Movement movement = new Movement();
            movement.setOrig(origAccount);
            movement.setDest(destAccount);
            movement.setAmount(amount);
            em.persist(movement);
            list.add(movement);
        }
        for (final Account account: accountMap.values()){
            em.persist(account);
        }
        return list;
    }
}
