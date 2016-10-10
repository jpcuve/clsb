package com.messio.clsb.session;

import com.messio.clsb.Frame;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.PayIn;
import com.messio.clsb.entity.Settlement;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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

    private List<Settlement> settlements = new ArrayList<>();


/*
    public void period(@Observes Frame frame) {
        for (Settlement settlement: frame.getSettlements()){
            LOGGER.info(String.format(" settlement: %s", settlement));
            settlements.add(settlement);
        }
        // say we settle at 01:00 (has to be a parameter)
        if (LocalTime.of(1, 0).equals(frame.getTo())){
            LOGGER.info("matching instructions");
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
                            LOGGER.info(String.format("matched: %s %s", a, b));
                            pair = new Settlement[]{ a, b };
                            break;
                        }
                    }
                }
                if (pair != null){
                    transfers.add(new Transfer(pair[0].getAccount(), pair[0].getAmount(), pair[1].getAccount()));
                    for (int i = 0; i < pair.length; i++){
                        settlements.remove(pair[i]);
                    }
                }
            } while (pair != null);
            facade.book(transfers);
        }
    }
*/

}
