package com.messio.clsb;

import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.session.ClsbFacade;
import com.messio.clsb.session.Scheduler;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jpc on 7/04/2016.
 */
@Path("/")
@Produces({"application/json"})
public class ClsbService {
    private static final Logger LOGGER = Logger.getLogger(ClsbService.class.getCanonicalName());
    @EJB
    private ClsbFacade facade;

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

}
