package com.messio.clsb;

import com.messio.clsb.entity.Account;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by jpc on 9/22/16.
 */
public class Ledger extends TreeMap<String, Position> {

    public Ledger() {
    }

    public Ledger(List<Account> accounts) {
        for (final Account account: accounts){
            this.put(account.getName(), account.getPosition());
        }
    }

    public boolean apply(Transfer transfer){
        return apply(transfer, (t) -> true);
    }

    public boolean apply(Transfer transfer, Predicate<Transfer> test){
        final Position origPosition = getOrDefault(transfer.getOrig(), Position.ZERO);
        final Position destPosition = getOrDefault(transfer.getDest(), Position.ZERO);
        boolean accept = test.test(transfer);
        if (accept){
            put(transfer.getOrig(), origPosition.subtract(transfer.getAmount()));
            put(transfer.getDest(), destPosition.add(transfer.getAmount()));
        }
        return accept;
    }

    public void output(final PrintStream pw){
        for (final Map.Entry<String, Position> entry: entrySet()){
            pw.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
        }
    }
}
