package legacyb3.core;

import legacyb3.data.CurrencyGroups;
import legacyb3.or2.DBEnumeration;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeMap;

/*
public class CurrencyGroupIterator implements Enumeration{
	private BinaryTree bt;
	private Enumeration e;
	
	public CurrencyGroupIterator(CurrencyGroups cgt, boolean dir) throws SQLException{
		bt = new BinaryTree(new CurrencyGroupPriorityComparator());
		for(DBEnumeration e = cgt.currencyGroups(); e.hasMoreElements();){
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
*/

public class CurrencyGroupIterator implements Iterator {
	private TreeMap tm;
	private Iterator i;
	
	public CurrencyGroupIterator(CurrencyGroups cgt, boolean dir) throws SQLException{
		tm = new TreeMap();
		for(DBEnumeration e = cgt.currencyGroups(); e.hasMoreElements();){
			CurrencyGroup cg = (CurrencyGroup)e.nextElement();
			tm.put(new Long(cg.getPriority()), cg);
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
	
