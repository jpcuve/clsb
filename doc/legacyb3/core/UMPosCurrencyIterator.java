package legacyb3.core;

import java.util.Iterator;
import java.util.TreeMap;


public class UMPosCurrencyIterator implements Iterator {
	public TreeMap tm;
	public Iterator i;
	
	public UMPosCurrencyIterator(UMPositionWithSM umPos){
		tm = new TreeMap();
		for(Iterator e = umPos.umBalances() ; e.hasNext();) {
			Currency ccy = ((UMBalance)e.next()).getCurrency();
			if(tm.get(ccy.getID()) == null) tm.put(ccy.getID(), ccy);
		}
		i = tm.values().iterator();
	}
	
	public UMPosCurrencyIterator(UMPositionWithSM umPos, BalanceType btyp){
		this(umPos, btyp, "All");
	}
	
	public UMPosCurrencyIterator(UMPositionWithSM umPos, BalanceType btyp, String p){
		tm = new TreeMap();
		for(Iterator e = umPos.umBalances(); e.hasNext();){
			UMBalance b = (UMBalance)e.next();
			if (b.getBalanceType() == btyp)
				if ((b.getAmount() < 0 && p.equals("Short")) || (b.getAmount() > 0 && p.equals("Long")) || (!p.equals("Short") && !p.equals("Long"))){
					 Currency ccy = b.getCurrency();
					 if(tm.get(ccy.getID()) == null) tm.put(ccy.getID(), ccy);
				}
		}
		i = tm.values().iterator();
	}
	
	public UMPosCurrencyIterator(UMPositionWithSM umPos, String p){
		tm = new TreeMap();
		for(Iterator e = umPos.umBalances(); e.hasNext();){
			UMBalance b = (UMBalance)e.next();
			if (b.getBalanceType().getID().equals("STD"))
				if ((b.getAmount() < 0 && p.equals("Short")) || (b.getAmount() > 0 && p.equals("Long")) || (!p.equals("Short") && !p.equals("Long"))){
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