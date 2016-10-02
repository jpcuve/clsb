package legacyb3.core;

import java.sql.*;
import java.util.Iterator;
import java.util.TreeMap;

import legacyb3.util.*;
import legacyb3.data.*;
import legacyb3.or2.*;

public class FcttIterator implements Iterator {
	 private TreeMap fctts;
	 private Iterator i;
	 
	 public FcttIterator(Currencies ct, boolean dir) throws SQLException{
		 fctts = new TreeMap();
		 for(DBEnumeration e1 = ct.currencies(); e1.hasMoreElements();){
			 Currency c = (Currency)e1.nextElement();
			 TimeOfDay tod = c.getFCTT();
			 fctts.put(new Integer(tod.getMinutes()), tod);
		 }
		 i = fctts.values().iterator();
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
