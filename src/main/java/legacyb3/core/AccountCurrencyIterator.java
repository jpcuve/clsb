package legacyb3.core;


import java.util.Iterator;
import java.util.TreeMap;

public class AccountCurrencyIterator implements Iterator {
	public TreeMap tm;
	public Iterator i;
		
	public AccountCurrencyIterator(Account a){
		tm = new TreeMap();
		for(Iterator i1 = a.balances(); i1.hasNext();){
			Balance b = (Balance)i1.next();
			Currency ccy = b.getCurrency();
			if(tm.get(ccy.getID()) == null) tm.put(ccy.getID(), ccy);
		}
		i = tm.values().iterator();
	}
	
	public AccountCurrencyIterator(Account a, BalanceType btyp){
		this(a, btyp, Account.NET );
	}
	
	public AccountCurrencyIterator(Account a, BalanceType btyp, int p){
		tm = new TreeMap();
		for(Iterator i1 = a.balances(); i1.hasNext();){
			Balance b = (Balance)i1.next();
			if (b.getBalanceType() == btyp)
				if ((b.getAmount() < 0 && p == Account.SHORT) || (b.getAmount() > 0 && p == Account.LONG) || (!(p == Account.SHORT) && !(p == Account.LONG))){
					 Currency ccy = b.getCurrency();
					 if(tm.get(ccy.getID()) == null) tm.put(ccy.getID(), ccy);
				}
		}
		i = tm.values().iterator();
	}
	
	public AccountCurrencyIterator(Account a, int p){
		tm = new TreeMap();
		for(Iterator i1 = a.balances(); i1.hasNext();){ 
			Balance b = (Balance)i1.next();
			if (b.getBalanceType() == BalanceType.std)
				if ((b.getAmount() < 0 && p == Account.SHORT) || (b.getAmount() > 0 && p == Account.LONG) || (!(p == Account.SHORT) && !(p == Account.LONG))){
					 Currency ccy = b.getCurrency();
					 if(tm.get(ccy.getID()) == null) tm.put(ccy.getID(), ccy);
				}
		}
		i = tm.values().iterator();
	}

	
	public boolean hasNext(){
		return i.hasNext();
	}
	
	public Object next(){
		return i.next();
	}
	
	public void remove(){
		i.remove();
	}
}