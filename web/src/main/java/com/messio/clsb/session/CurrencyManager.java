package com.messio.clsb.session;

import com.messio.clsb.entity.Currency;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jpc on 10/27/16.
 */
@Singleton(name = "clsb/currency-manager")
@LocalBean
@Path("/")
@Produces({"application/json"})
public class CurrencyManager {
    private static final Logger LOGGER = Logger.getLogger(CurrencyManager.class.getCanonicalName());
    @EJB
    private ClsbFacade facade;

    @GET
    @Path("/currencies")
    public List<Currency> currencies(){
        return facade.findCurrencies(facade.findBank());
    }
}
