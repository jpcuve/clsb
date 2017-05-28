package com.messio.clsb.session;

import com.messio.clsb.Ledger;
import com.messio.clsb.Position;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Account;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.PayOut;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 9/29/16.
 */
@Singleton(name = "clsb/pay-out-manager")
@LocalBean
public class PayOutManager {
    public static final Logger LOGGER = Logger.getLogger(PayOutManager.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;

    public List<Transfer> computePayOuts(String iso){
        final Bank bank = facade.findBank();
        final List<PayOut> payOuts = new ArrayList<>();
        for (final Account account: facade.findAccounts(bank)){
            final Position longPosition = account.getPosition().xlong().filter(iso);
            if (longPosition.isLong()){
                final PayOut payOut = new PayOut();
                payOut.setReference(String.format("pay-out %s", iso));
                payOut.setAccount(account.getName());
                payOut.setAmount(longPosition);
                payOuts.add(payOut);
            }
        }
        return payOuts.stream()
                .map(Transfer::new)
                .collect(Collectors.toList());
    }

    public List<PayOut> computePayOutsBuild1(final Bank bank, final List<Transfer> projectedTransfers){
        final List<PayOut> payOuts = new ArrayList<>();
        // compute net projected position
        final Ledger ledger = new Ledger(facade.findAccounts(bank), projectedTransfers);
        return payOuts;
    }

}
