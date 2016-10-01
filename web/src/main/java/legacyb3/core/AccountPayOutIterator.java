package legacyb3.core;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeMap;

import legacyb3.data.*;
import legacyb3.or2.*;

public class AccountPayOutIterator implements Iterator {
	private TreeMap tm;
	private Iterator i;
	
	public AccountPayOutIterator(Accounts at) throws SQLException{
		tm = new TreeMap();
		for(DBEnumeration e = at.settlementMemberAccounts(); e.hasMoreElements();){
			Account a = (Account)e.nextElement();
			double d = a.getAvailablePayOut();
			if(d != 0) tm.put(new Double(d), a);
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

