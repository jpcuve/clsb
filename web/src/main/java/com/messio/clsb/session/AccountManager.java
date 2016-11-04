package com.messio.clsb.session;

import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.entity.Movement;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    private Bank bank;

    @PostConstruct
    public void init(){
        this.bank = facade.findBank();
    }

    public void reset(){
        facade.deleteMovements();
        for (final Account account: facade.findAccounts(bank)){
            account.setPosition(Position.ZERO);
            facade.update(account);
        }
        final Account mirror = getMirror();
    }

    @GET
    @Path("/mirror")
    public Account getMirror(){
        return facade.findAccount(bank, Account.MIRROR_NAME);
    }

    public List<Movement> book(LocalTime when, List<Transfer> transfers){
        final List<Movement> list = new ArrayList<>();
        final Map<String, Account> accountMap = accounts().stream().collect(Collectors.toMap(Account::getName, Function.identity()));
        final Set<String> modified = new HashSet<>();
        for (final Transfer transfer: transfers){
            final Account origAccount = accountMap.get(transfer.getOrig());
            final Account destAccount = accountMap.get(transfer.getDest());
            if (origAccount != null && destAccount != null){
                final Position amount = transfer.getAmount();
                final Position origPosition = origAccount.getPosition();
                final Position destPosition = destAccount.getPosition();
                origAccount.setPosition(origPosition == null ? amount.negate() : origPosition.subtract(amount));
                destAccount.setPosition(destPosition == null ? amount : destPosition.add(amount));
                modified.add(transfer.getOrig());
                modified.add(transfer.getDest());
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
        accountMap.values().stream().filter(a -> a != null && modified.contains(a.getName())).forEach(a -> facade.update(a));
        return list;
    }

    @GET
    @Path("/accounts")
    public List<Account> accounts(){
        return facade.findAccounts(bank);
    }

    @GET
    @Path("/movements")
    public List<Movement> movements(@QueryParam("accountId") Long accountId){
        return facade.findMovements(facade.find(Account.class, accountId));
    }
}
