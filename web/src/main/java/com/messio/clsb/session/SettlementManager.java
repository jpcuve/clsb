package com.messio.clsb.session;

import com.messio.clsb.Ledger;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Bank;
import com.messio.clsb.entity.Settlement;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jpc on 22-09-16.
 */
@Singleton(name = "clsb/settlement-manager")
@LocalBean
public class SettlementManager {
    public static final Logger LOGGER = Logger.getLogger(SettlementManager.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;

    public List<Transfer> buildSettlementQueue(final List<Settlement> settlements){
        final List<Transfer> transfers = new ArrayList<>();
        Settlement[] pair;
        do {
            pair = null;
            for (int i = 0; i < settlements.size(); i++){
                for (int j = i + 1; j < settlements.size(); j++){
                    final Settlement a = settlements.get(i);
                    final Settlement b = settlements.get(j);
                    boolean match = a.getCounterParty().equals(b.getAccount()) && a.getAccount().equals(b.getCounterParty()) && a.getAmount().equals(b.getAmount().negate());
                    if (match){
                        LOGGER.info(String.format("Matched: %s %s", a, b));
                        pair = new Settlement[]{ a, b };
                        break;
                    }
                }
            }
            if (pair != null){
                transfers.add(new Transfer(String.format("%s<>%s", pair[0].getReference(), pair[1].getReference()), pair[0].getAccount(), pair[0].getAmount(), pair[1].getAccount()));
                for (int i = 0; i < pair.length; i++){
                    settlements.remove(pair[i]);
                }
            }
        } while (pair != null);
        return transfers;
    }

    public List<Transfer> settleUnconditionally(final List<Transfer> transfers){
        return transfers;
    }

    public List<Transfer> settleWithShortPositionLimit(final List<Transfer> transfers, Ledger ledger){
        return transfers;
    }
}
