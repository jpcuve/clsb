package com.messio.clsb.session;

import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.entity.Movement;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by jpc on 25-09-16.
 */
@Stateless(name = "clsb/clsb-facade")
@LocalBean
public class ClsbFacade {
    @PersistenceContext
    private EntityManager em;
    private Bank bank;

    @PostConstruct
    public void init(){
        this.bank = loadBank(Bank.DEFAULT_NAME);
        Arrays.asList("EUR", "USD", "JPY").forEach(this::loadCurrency);
    }

    public Bank loadBank(String name){
        return em.createNamedQuery(Bank.BANK_BY_NAME, Bank.class).setParameter("name", name).getResultList().stream().findFirst().orElseGet(() -> {
            final Bank b = new Bank();
            em.persist(b);
            return b;
        });
    }

    public Account loadAccount(final String name){
        return em.createNamedQuery(Account.ACCOUNT_BY_NAME_BY_BANK, Account.class).setParameter("name", name).setParameter("bank", this.bank).getResultList().stream().findFirst().orElseGet(() -> {
            final Account a = new Account();
            a.setBank(this.bank);
            a.setName(name);
            a.setPosition(Position.ZERO);
            em.persist(a);
            return a;
        });
    }

    public Currency loadCurrency(final String iso){
        return em.createNamedQuery(Currency.CURRENCY_BY_ISO_BY_BANK, Currency.class).setParameter("iso", iso).setParameter("bank", this.bank).getResultList().stream().findFirst().orElseGet(() -> {
            final Currency c = new Currency();
            c.setBank(this.bank);
            c.setIso(iso);
            em.persist(c);
            return c;
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
