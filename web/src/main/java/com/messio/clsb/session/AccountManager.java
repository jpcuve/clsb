package com.messio.clsb.session;

import com.messio.clsb.Ledger;
import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Movement;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.*;
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
    private ClsbFacade facade;
    private Bank bank;

    public AccountManager() {
    }

    @Inject
    public AccountManager(ClsbFacade facade) {
        this.facade = facade;
        this.bank = facade.findBank();
    }

    public void reset(){
        facade.deleteMovements();
        for (final Account account: facade.findAccounts(bank)){
            account.setPosition(Position.ZERO);
            facade.update(account);
        }
    }

    @GET
    @Path("/accounts/{name}")
    public Account getAccount(@PathParam("name") final String name){
        return facade.findAccount(bank, name);
    }

    @GET
    @Path("/positions")
    public Ledger getPositions(){
        return facade.findAccounts(bank).stream().collect(Collectors.toMap(Account::getName, Account::getPosition, Position::add, Ledger::new));
    }

    public List<Movement> book(LocalTime when, List<Transfer> transfers){
        final List<Movement> list = new ArrayList<>();
        final Map<String, Account> accountMap = facade.findAccounts(bank).stream().collect(Collectors.toMap(Account::getName, Function.identity()));
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
    @Path("/movements/{name}")
    public List<Movement> movements(@PathParam("name") String name){
        final List<Movement> movements = new ArrayList<>();
        movements.addAll(facade.findMovements(facade.findAccount(bank, name), true));
        movements.addAll(facade.findMovements(facade.findAccount(bank, name), false));
        return movements;
    }
}
