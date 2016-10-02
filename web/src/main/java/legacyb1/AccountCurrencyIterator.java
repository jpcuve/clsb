package legacyb1;

import java.util.*;

public class AccountCurrencyIterator implements Enumeration{
	public BTree bt;
	public Enumeration e;
	
	public AccountCurrencyIterator(Account a){
		bt = new BTree(new CurrencyPriorityComparator());
		for(e = a.balances(); e.hasMoreElements();) 
			bt.addElement(((Balance)e.nextElement()).getCurrency());
		e = bt.elements();
	}
	
	public AccountCurrencyIterator(Account a, BalanceType btyp){
		this(a, btyp, "All");
	}
	
	public AccountCurrencyIterator(Account a, BalanceType btyp, String p){
		bt = new BTree(new CurrencyPriorityComparator());
		for(e = a.balances(); e.hasMoreElements();){
			Balance b = (Balance)e.nextElement();
			if (b.getBalanceType() == btyp)
				if ((b.getAmount() < 0 && p.equals("Short")) || (b.getAmount() > 0 && p.equals("Long")) || (!p.equals("Short") && !p.equals("Long")))
					bt.addElement(b.getCurrency());
		}
		e = bt.elements();
	}
	
	public AccountCurrencyIterator(Account a, String p){
		bt = new BTree(new CurrencyPriorityComparator());
		for(e = a.balances(); e.hasMoreElements();){
			Balance b = (Balance)e.nextElement();
			if (b.getBalanceType().getID().equals("STD"))
				if ((b.getAmount() < 0 && p.equals("Short")) || (b.getAmount() > 0 && p.equals("Long")) || (!p.equals("Short") && !p.equals("Long")))
					bt.addElement(b.getCurrency());
		}
		e = bt.elements();
	}

	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement(){
		return e.nextElement();
	}
	
}