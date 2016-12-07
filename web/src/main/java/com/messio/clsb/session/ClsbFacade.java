package com.messio.clsb.session;

import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.entity.Movement;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by jpc on 25-09-16.
 */
@Stateless(name = "clsb/clsb-facade")
@LocalBean
public class ClsbFacade {
    private static final Logger LOGGER = Logger.getLogger(ClsbFacade.class.getCanonicalName());
    @PersistenceContext
    private EntityManager em;

    public <E> void create(E e){
        em.persist(e);
    }

    public <E> E update(E e){
        return em.merge(e);
    }

    public <E, ID> E find(Class<E> aClass, ID id){
        return em.find(aClass, id);
    }

    public Bank findBank(){
        return em.createNamedQuery(Bank.BANK_ALL, Bank.class).getResultList().stream().findFirst().orElse(null);
    }

    public List<Currency> findCurrencies(final Bank bank){
        return em.createNamedQuery(Currency.CURRENCY_BY_BANK, Currency.class).setParameter("bank", bank).getResultList();
    }

    public List<Account> findAccounts(final Bank bank){
        return em.createNamedQuery(Account.ACCOUNT_BY_BANK, Account.class).setParameter("bank", bank).getResultList();
    }

    public List<Movement> findMovements(Account account, boolean origin) {
        if (account == null){
            return Collections.emptyList();
        }
        return em.createNamedQuery(origin ? Movement.MOVEMENT_BY_ORIG_ACCOUNT : Movement.MOVEMENT_BY_DEST_ACCOUNT, Movement.class).setParameter("account", account).getResultList();
    }

    public Account findAccount(final Bank bank, final String name){
        return em.createNamedQuery(Account.ACCOUNT_BY_NAME_BY_BANK, Account.class).setParameter("name", name).setParameter("bank", bank).getResultList().stream().findFirst().orElseGet(() -> null);
    }


    public int deleteMovements() {
        return em.createNamedQuery(Movement.MOVEMENT_DELETE).executeUpdate();
    }
}
