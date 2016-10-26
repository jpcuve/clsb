package com.messio.clsb;

import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.entity.Movement;
import com.messio.clsb.session.ClsbFacade;
import com.messio.clsb.session.Scheduler;
import com.messio.clsb.util.script.Environment;
import com.messio.clsb.util.script.Parser;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 7/04/2016.
 */
@Path("/")
@Produces({"application/json"})
public class ClsbService extends Environment {
    private static final Logger LOGGER = Logger.getLogger(ClsbService.class.getCanonicalName());
    @EJB
    private ClsbFacade facade;
    @EJB
    private Scheduler scheduler;

    @Override
    public Object call(String function, List<Object> arguments) {
        LOGGER.info(String.format("%s(%s)", function, arguments.stream().map(Parser::toString).collect(Collectors.joining(","))));
        switch(function){
            case "reset":
                scheduler.reset();
                return null;
            case "step":
                scheduler.step();
                return null;
        }
        return null;
    }

    @GET
    @Path("/bank")
    public Bank bank(){
        return facade.findBank();
    }

    @GET
    @Path("/accounts")
    public List<Account> accounts(){
        return facade.findAccounts(bank());
    }

    @GET
    @Path("/currencies")
    public List<Currency> currencies(){
        return facade.findCurrencies(bank());
    }

    @GET
    @Path("/movements")
    public List<Movement> movements(@QueryParam("accountId") Long accountId){
        return facade.findMovements(facade.find(Account.class, accountId));
    }

    @GET
    @Path("/command/{cmd}")
    public String command(@PathParam("cmd") String cmd){
        try{
            return Parser.toString(eval(cmd));
        } catch (ParseException e){
            LOGGER.severe(e.getMessage());
            return e.getMessage();
        }
    }

}
