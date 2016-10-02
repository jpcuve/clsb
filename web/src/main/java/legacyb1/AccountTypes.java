package legacyb1;

import java.util.*;
import java.sql.*;

public class AccountTypes extends DBCachedTable{
	
	// inner classes
	private class ID extends DBString{
		public ID(){super(true, "ID");}
		public void set(DBRecord rec, String value){((AccountType)rec).setID(value);}
		public String get(DBRecord rec){	return ((AccountType)rec).getID();}
	}
	private class Name extends DBString{
		public Name(){super(false, "Name");}
		public void set(DBRecord rec, String value){((AccountType)rec).setName(value);}
		public String get(DBRecord rec){	return ((AccountType)rec).getName();}
	}
	
		// constructors
	
	public AccountTypes(Connection con) throws SQLException, DBIOException{
		super(con, "AccountTypes", new AccountType(null));
		addColumnDefinition(new ID());
		addColumnDefinition(new Name());
	}
	
		// accessors
	
	public AccountType get(String id) throws IllegalAccountTypeException{
		AccountType at = (AccountType)find(new AccountType(id));
		if (at == null) throw new IllegalAccountTypeException("[" + id + "]");
		return at;
	}
	
	public Enumeration accountTypes(){
		clearConstraints();
		return records();
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached account types=\n" + super.toString();
	}
	
	// miscellaneous
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException, IllegalCurrencyException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		AccountTypes att = new AccountTypes(con);
		System.out.println(att);
	}
	
}