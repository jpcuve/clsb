package legacyb1;

import java.util.*;
import java.sql.*;

public class CurrencyGroups extends DBCachedTable{
	
	// inner classes
	private class ID extends DBString{
		public ID(){super(true, "ID");}
		public void set(DBRecord rec, String value){((CurrencyGroup)rec).setName(value);}
		public String get(DBRecord rec){	return ((CurrencyGroup)rec).getName();}
	}
	private class Priority extends DBLong{
		public Priority(){super(false, "Priority");}
		public void set(DBRecord rec, long value){((CurrencyGroup)rec).setPriority(value);}
		public long get(DBRecord rec){	return ((CurrencyGroup)rec).getPriority();}
	}
	
	// constructors
	
	public CurrencyGroups(Connection con) throws SQLException, DBIOException{
		super(con, "CurrencyGroups", new CurrencyGroup(null));
		addColumnDefinition(new ID());
		addColumnDefinition(new Priority());
	}
	
	// accessors
	
	public CurrencyGroup get(String name) throws IllegalCurrencyGroupException{
		CurrencyGroup cg = (CurrencyGroup)find(new CurrencyGroup(name));
		if (cg == null) throw new IllegalCurrencyGroupException("[" + name + "]");
		return cg;
	}
	
	public Enumeration currencyGroups(){
		clearConstraints();
		return records();
	}
	
	public Enumeration currencyGroupsPerPriority(boolean dir){
		return new CurrencyGroupIterator(this, dir);
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached currency groups=\n" + super.toString();
	}
	
	// miscellaneous
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException, IllegalCurrencyGroupException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CurrencyGroups cgt = new CurrencyGroups(con);
		System.out.println("Currency groups");
		for(Enumeration e = cgt.currencyGroups(); e.hasMoreElements();){
			System.out.println(e.nextElement());
		}
		System.out.println("Currency groups per priority");
		for(Enumeration e = cgt.currencyGroupsPerPriority(true); e.hasMoreElements();){
			System.out.println(e.nextElement());
		}
		System.out.println(cgt);
	}
	
}