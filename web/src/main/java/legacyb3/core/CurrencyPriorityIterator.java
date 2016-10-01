package legacyb3.core;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import legacyb3.data.*;
import legacyb3.util.*;

public class CurrencyPriorityIterator implements Enumeration {
	private Vector v;
	private Enumeration e;
	
	public CurrencyPriorityIterator(Currencies ct, CurrencyGroups cgt, boolean dir) throws SQLException{
		v = new Vector();
		/*
		for(Enumeration e1 = cgt.currencyGroupsPerPriority(dir); e1.hasMoreElements();){
			CurrencyGroup cg = (CurrencyGroup)e1.nextElement();
		*/
		for(Iterator i1 = cgt.currencyGroupsPerPriority(dir); i1.hasNext();){
			CurrencyGroup cg = (CurrencyGroup)i1.next();
			/*
			BinaryTree bt = new BinaryTree(new CurrencyPriorityComparator());
			for(Enumeration e2 = cg.currencies(); e2.hasMoreElements();)
			*/
			TreeMap tm = new TreeMap();
			for(Iterator i2 = cg.currencies(); i2.hasNext();){
				Currency ccy = (Currency)i2.next();
				Double d = new Double(ccy.getWeightedBalance());
				Vector v1 = (Vector)tm.get(d);
				if(v1 == null){
					v1 = new Vector();
					tm.put(d, v1);
				}
				v1.addElement(ccy);
			}
			/*
			for(Enumeration e2 = bt.elements(dir); e2.hasMoreElements();)
				v.addElement((Currency)e2.nextElement());
			*/
			for(Iterator i2 = tm.values().iterator(); i2.hasNext();){
				Vector v1 = (Vector)i2.next();
				for(Enumeration e3 = v1.elements(); e3.hasMoreElements();) v.addElement((Currency)e3.nextElement());
			}
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
	