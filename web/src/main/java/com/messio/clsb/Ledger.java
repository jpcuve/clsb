package com.messio.clsb;

import com.messio.clsb.entity.Account;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by jpc on 9/22/16.
 */
public class Ledger {
    private Map<String, Position> positions = new TreeMap<>();

    public Ledger() {
    }

    public Ledger(List<Account> accounts) {
        for (final Account account: accounts){
            positions.put(account.getName(), account.getPosition());
        }
    }

    public Set<String> getAccountNames(){
        return positions.keySet();
    }

    public Position getPosition(String accountName){
        return positions.getOrDefault(accountName, Position.ZERO);
    }

    public boolean apply(Transfer transfer){
        return apply(transfer, (t) -> true);
    }

    public boolean apply(Transfer transfer, Predicate<Transfer> test){
        final Position origPosition = getPosition(transfer.getOrig());
        final Position destPosition = getPosition(transfer.getDest());
        boolean accept = test.test(transfer);
        if (accept){
            positions.put(transfer.getOrig(), origPosition.subtract(transfer.getAmount()));
            positions.put(transfer.getDest(), destPosition.add(transfer.getAmount()));
        }
        return accept;
    }

    public void output(final PrintStream pw){
        for (final Map.Entry<String, Position> entry: positions.entrySet()){
            pw.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
        }
    }
}
