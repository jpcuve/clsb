package legacyb1;

import java.util.*;

public class CurrencyFCTTIterator implements Enumeration{
	private BTree bt;
	private Enumeration e;
	
	public CurrencyFCTTIterator(Currencies ct, boolean dir){
		bt = new BTree(new CurrencyFCTTComparator());
		for(Enumeration e1 = ct.currencies(); e1.hasMoreElements();)
			bt.addElement((Currency)e1.nextElement());
		e = bt.elements(dir);
	}
	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement(){
		return e.nextElement();
	}
}