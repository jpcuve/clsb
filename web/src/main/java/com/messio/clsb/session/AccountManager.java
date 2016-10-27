package com.messio.clsb.session;

import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.entity.Movement;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jpc on 26-10-16.
 */
@Singleton(name = "clsb/account-manager")
@LocalBean
@Path("/")
@Produces({"application/json"})
public class AccountManager {
    public static final Logger LOGGER = Logger.getLogger(AccountManager.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;

    public void reset(){
        final Bank bank = facade.findBank();
        facade.deleteMovements();
        for (final Account account: facade.findAccounts(bank)){
            account.setPosition(Position.ZERO);
            facade.update(account);
        }
    }

    public List<Movement> book(LocalTime when, List<Transfer> transfers){
        final Bank bank = facade.findBank();
        final List<Movement> list = new ArrayList<>();
        final Map<String, Account> accountMap = new HashMap<>();
        for (final Transfer transfer: transfers){
            final Account origAccount = accountMap.computeIfAbsent(transfer.getOrig(), a -> facade.findAccount(bank, a));
            final Account destAccount = accountMap.computeIfAbsent(transfer.getDest(), a -> facade.findAccount(bank, a));
            if (origAccount != null && destAccount != null){
                final Position amount = transfer.getAmount();
                final Position origPosition = origAccount.getPosition();
                final Position destPosition = destAccount.getPosition();
                origAccount.setPosition(origPosition == null ? amount.negate() : origPosition.subtract(amount));
                destAccount.setPosition(destPosition == null ? amount : destPosition.add(amount));
                final Movement movement = new Movement();
                movement.setInformation(transfer.getInformation());
                movement.setWhen(when);
                movement.setOrig(origAccount);
                movement.setDest(destAccount);
                movement.setAmount(amount);
                LOGGER.info(String.format("Movement: %s", movement));
                facade.create(movement);
                list.add(movement);
            }
        }
        accountMap.values().stream().filter(a -> a != null).forEach(a -> facade.update(a));
        return list;
    }

    @GET
    @Path("/accounts")
    public List<Account> accounts(){
        return facade.findAccounts(facade.findBank());
    }

    @GET
    @Path("/movements")
    public List<Movement> movements(@QueryParam("accountId") Long accountId){
        return facade.findMovements(facade.find(Account.class, accountId));
    }


}
