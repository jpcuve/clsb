package legacyb1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class BalanceTypes extends DBCachedTable {
	
	// inner classes
	private class ID extends DBString {
		public ID(){super(true, "ID");}
		public void set(DBRecord rec, String value){((BalanceType)rec).setID(value);}
		public String get(DBRecord rec){	return ((BalanceType)rec).getID();}
	}
	private class Name extends DBString {
		public Name(){super(false, "Name");}
		public void set(DBRecord rec, String value){((BalanceType)rec).setName(value);}
		public String get(DBRecord rec){	return ((BalanceType)rec).getName();}
	}
	
		// constructors
	
	public BalanceTypes(Connection con) throws SQLException{
		super(con, "BalanceTypes", new BalanceType("Dummy"));
		addColumnDefinition(new ID());
		addColumnDefinition(new Name());
	}
	
		// accessors
	
	public BalanceType get(String id) throws IllegalBalanceTypeException {
		BalanceType bt = null;
		BalanceType pattern = new BalanceType(id);
		try{
			bt = (BalanceType)find(pattern);
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
		if (bt == null) throw new IllegalBalanceTypeException("[" + id + "]");
		return bt;
	}
	
	public Enumeration balanceTypes(){
		clearConstraints();
		Enumeration e = null;
		try{
			e = records();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
		return e;
	}
		
	// miscellaneous
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException, IllegalCurrencyException {
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		BalanceTypes att = new BalanceTypes(con);
		att.load();
		System.out.println(att);
	}
	
}