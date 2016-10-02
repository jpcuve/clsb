package legacyb1;

import java.util.*;

public class CurrencyPriorityIterator implements Enumeration{
	private Vector v;
	private Enumeration e;
	
	public CurrencyPriorityIterator(Currencies ct, CurrencyGroups cgt, boolean dir){
		v = new Vector();
		for(Enumeration e1 = cgt.currencyGroupsPerPriority(dir); e1.hasMoreElements();){
			CurrencyGroup cg = (CurrencyGroup)e1.nextElement();
			BTree bt = new BTree(new CurrencyPriorityComparator());
			for(Enumeration e2 = cg.currencies(); e2.hasMoreElements();)
				bt.addElement((Currency)e2.nextElement());
			for(Enumeration e2 = bt.elements(dir); e2.hasMoreElements();)
				v.addElement((Currency)e2.nextElement());
		}
		e = v.elements();
	}
	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement(){
		return e.nextElement();
	}
}
	