package legacyb1;

import java.util.*;

public class CurrencyGroupIterator implements Enumeration{
	private BTree bt;
	private Enumeration e;
	
	public CurrencyGroupIterator(CurrencyGroups cgt, boolean dir){
		bt = new BTree(new CurrencyGroupPriorityComparator());
		for(Enumeration e = cgt.currencyGroups(); e.hasMoreElements();){
			bt.addElement(e.nextElement());
		}
		e = bt.elements(dir);
	}
	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement(){
		return e.nextElement();
	}
}