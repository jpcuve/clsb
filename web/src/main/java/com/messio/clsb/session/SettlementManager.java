package com.messio.clsb.session;

import com.messio.clsb.Ledger;
import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Instruction;
import com.messio.clsb.entity.Settlement;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 22-09-16.
 */
@Singleton(name = "clsb/settlement-manager")
@LocalBean
@Path("/")
@Produces({"application/json"})
public class SettlementManager {
    public static final Logger LOGGER = Logger.getLogger(SettlementManager.class.getCanonicalName());
    private List<Settlement> settlements;

    public SettlementManager() {
    }

    @Inject
    public SettlementManager(final List<Instruction> instructions){
        this.settlements = instructions.stream()
                .filter(i -> i instanceof Settlement)
                .map(i -> (Settlement) i)
                .collect(Collectors.toList());
    }

    public List<Transfer> buildSettlementQueue(LocalTime now){
        final List<Transfer> transfers = new ArrayList<>();
        Settlement[] pair;
        do {
            pair = null;
            for (int i = 0; i < settlements.size(); i++){
                for (int j = i + 1; j < settlements.size(); j++){
                    final Settlement a = settlements.get(i);
                    final Settlement b = settlements.get(j);
                    if (a.getTransferred() == null && b.getTransferred() == null && a.getWhen().isBefore(now) && b.getWhen().isBefore(now)){
                        boolean match = a.getCounterParty().equals(b.getAccount()) && a.getAccount().equals(b.getCounterParty()) && a.getAmount().equals(b.getAmount().negate());
                        if (match){
                            LOGGER.info(String.format("Matched: %s %s", a, b));
                            pair = new Settlement[]{ a, b };
                            break;
                        }
                    }
                }
            }
            if (pair != null){
                transfers.add(new Transfer(pair[0], pair[1]));
                settlements.removeAll(Arrays.asList(pair));
            }
        } while (pair != null);
        return transfers;
    }

    public List<Transfer> settleUnconditionally(final List<Transfer> transfers){
        return transfers;
    }

    public List<Transfer> settleWithShortPositionLimit(final List<Transfer> transfers, Ledger positions, Ledger shortPositionLimits){
        return transfers;
    }

    @GET
    @Path("/unsettled")
    public Ledger getPositions(){
/*
        buildSettlementQueue()
        return facade.findAccounts(bank).stream().collect(Collectors.toMap(Account::getName, Account::getPosition, Position::add, Ledger::new));
*/
        return new Ledger();
    }

}
