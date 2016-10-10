package com.messio.clsb.session;

import com.messio.clsb.Frame;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.PayOut;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jpc on 9/29/16.
 */
@Singleton(name = "clsb/pay-out-manager")
@LocalBean
public class PayOutManager {
    public static final Logger LOGGER = Logger.getLogger(PayOutManager.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;
    @PersistenceContext
    private EntityManager em;


/*
    public void period(@Observes Frame frame) {
        if (LocalTime.of(1, 30).equals(frame.getTo())){
            final List<PayOut> payOuts = new ArrayList<>();
            for (final Account account: em.createNamedQuery(Account.ACCOUNT_ALL, Account.class).getResultList()){
                if (!Transfer.MIRROR_NAME.equals(account.getName())){
                    final PayOut payOut = new PayOut();
                    payOut.setAccount(account.getName());
                    payOut.setWhen(frame.getTo());
                    payOut.setAmount(account.getPosition());
                    payOuts.add(payOut);
                    LOGGER.info(String.format(" pay-out: %s", payOut));
                }

            }
            LOGGER.info("booking pay-outs");
            final List<Transfer> transfers = new ArrayList<>();
            for (final PayOut payOut: payOuts){
                transfers.add(new Transfer(payOut.getAccount(), payOut.getAmount()));
            }
            facade.book(null, transfers);
        }
    }
*/
}
