package com.messio.clsb;

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
@Stateless
public class ClsbService {
    private static final Logger LOGGER = Logger.getLogger(ClsbService.class.getCanonicalName());

    @PersistenceContext(unitName = "example")
    private EntityManager em;

}
