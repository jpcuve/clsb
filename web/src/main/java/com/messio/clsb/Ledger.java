package com.messio.clsb;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by jpc on 9/22/16.
 */
public class Ledger {
    private Map<String, Position> positions = new TreeMap<>();

    public Set<String> getAccounts(){
        return positions.keySet();
    }

    public Position getPosition(String account){
        return positions.getOrDefault(account, Position.ZERO);
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

    public List<Transfer> getPayIns(){
        return positions.entrySet().stream().filter(e -> !Transfer.MIRROR_NAME.equals(e.getKey())).map(e -> new Transfer(e.getValue().negate(), e.getKey())).collect(Collectors.toList());
    }

    public List<Transfer> getPayOuts(){
        return positions.entrySet().stream().filter(e -> !Transfer.MIRROR_NAME.equals(e.getKey())).map(e -> new Transfer(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public void output(final PrintStream pw){
        for (final Map.Entry<String, Position> entry: positions.entrySet()){
            pw.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
        }
    }

    public static void main(String[] args) {
        final Ledger ledger = new Ledger();
        ledger.apply(new Transfer(new Position("EUR", 150), "annie"));
        System.out.println("after pay-in");
        ledger.output(System.out);
        ledger.apply(new Transfer("annie", new Position("EUR", 100), "jpc"));
        System.out.println("after settlement");
        ledger.output(System.out);
        System.out.println("payouts:");
        List<Transfer> payOuts = ledger.getPayOuts();
        for (final Transfer transfer: payOuts){
            System.out.println("  " + transfer);
        }
        payOuts.forEach(ledger::apply);
        System.out.println("after payout");
        ledger.output(System.out);
    }
}
