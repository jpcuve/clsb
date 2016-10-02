package legacyb1;

import java.util.*;

public class AccountIterator implements Enumeration{
	private BTree bt;
	private Enumeration e;
	
	public AccountIterator(Accounts at){
		bt = new BTree(new AccountAvailablePayOutComparator());
		for(Enumeration e = at.accounts("SM"); e.hasMoreElements();){
			Account a = (Account)e.nextElement();
			if(a.getAvailablePayOut() != 0)
				bt.addElement(a);
		}
		e = bt.elements(false);
	}
	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement(){
		return e.nextElement();
	}
}
	