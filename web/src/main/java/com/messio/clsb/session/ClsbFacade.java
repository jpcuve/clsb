package com.messio.clsb.session;

import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.entity.Movement;
import com.messio.clsb.model.BankModel;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
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

    public Bank build(BankModel bankModel){
        final Bank bank = bankModel.getBank();
        em.persist(bank);
        for (final Currency currency: bankModel.getCurrencies()){
            currency.setBank(bank);
            em.persist(currency);
        }
        for (final Account account: bankModel.getAccounts()){
            account.setBank(bank);
            em.persist(account);
        }
        return bank;
    }

    public List<Currency> findCurrencies(final Bank bank){
        return em.createNamedQuery(Currency.CURRENCY_BY_BANK, Currency.class).setParameter("bank", bank).getResultList();
    }

    public List<Account> findAccounts(final Bank bank){
        return em.createNamedQuery(Account.ACCOUNT_BY_BANK, Account.class).setParameter("bank", bank).getResultList();
    }

    public Account findAccount(final Bank bank, final String name){
        return em.createNamedQuery(Account.ACCOUNT_BY_NAME_BY_BANK, Account.class).setParameter("name", name).setParameter("bank", bank).getResultList().stream().findFirst().orElseGet(() -> null);
    }

    public List<Movement> book(Bank bank, List<Transfer> transfers){
        final List<Movement> list = new ArrayList<>();
        final Map<String, Account> accountMap = new HashMap<>();
        for (final Transfer transfer: transfers){
            final Account origAccount = accountMap.computeIfAbsent(transfer.getOrig(), a -> findAccount(bank, a));
            final Account destAccount = accountMap.computeIfAbsent(transfer.getDest(), a -> findAccount(bank, a));
            if (origAccount != null && destAccount != null){
                final Position amount = transfer.getAmount();
                final Position origPosition = origAccount.getPosition();
                final Position destPosition = destAccount.getPosition();
                origAccount.setPosition(origPosition == null ? amount.negate() : origPosition.subtract(amount));
                destAccount.setPosition(destPosition == null ? amount : destPosition.add(amount));
                final Movement movement = new Movement();
                movement.setInformation(transfer.getInformation());
                movement.setOrig(origAccount);
                movement.setDest(destAccount);
                movement.setAmount(amount);
                LOGGER.info(String.format("Movement: %s", movement));
                em.persist(movement);
                list.add(movement);
            }
        }
        accountMap.values().stream().filter(a -> a != null).forEach(a -> em.persist(a));
        return list;
    }
}
