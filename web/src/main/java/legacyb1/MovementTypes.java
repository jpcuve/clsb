package legacyb1;

import java.util.*;
import java.sql.*;

public class MovementTypes extends DBCachedTable{
	private Hashtable movementTypes;
	private AccountTypes att;
	private BalanceTypes btt;
	
	// inner classes
	
	private class ID extends DBString{
		public ID(){super(true, "ID");}
		public void set(DBRecord rec, String value){((MovementType)rec).setID(value);}
		public String get(DBRecord rec){	return ((MovementType)rec).getID();}
	}
	
	private class FromAccountTypeFK extends DBString{
		public FromAccountTypeFK(){super(false, "From Account Type FK");}
		public void set(DBRecord rec, String value){
			try{
				((MovementType)rec).setFromAccountType(att.get(value));
			}catch(IllegalAccountTypeException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((MovementType)rec).getFromAccountType().getID();}
	}
	
	private class ToAccountTypeFK extends DBString{
		public ToAccountTypeFK(){super(false, "To Account Type FK");}
		public void set(DBRecord rec, String value){
			try{
				((MovementType)rec).setToAccountType(att.get(value));
			}catch(IllegalAccountTypeException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((MovementType)rec).getToAccountType().getID();}
	}
	
	private class BalanceTypeFK extends DBString{
		public BalanceTypeFK(){super(false, "Balance Type FK");}
		public void set(DBRecord rec, String value){
			try{
				((MovementType)rec).setBalanceType(btt.get(value));
			}catch(IllegalBalanceTypeException ex){
				ex.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((MovementType)rec).getBalanceType().getID();}
	}

	private class Name extends DBString{
		public Name(){super(false, "Name");}
		public void set(DBRecord rec, String value){((MovementType)rec).setName(value);}
		public String get(DBRecord rec){	return ((MovementType)rec).getName();}
	}
	

		// constructors
	
	public MovementTypes(Connection con, AccountTypes att, BalanceTypes btt) throws SQLException{
		super(con, "MovementTypes", new MovementType(null));
		addColumnDefinition(new ID());
		addColumnDefinition(new FromAccountTypeFK());
		addColumnDefinition(new ToAccountTypeFK());
		addColumnDefinition(new BalanceTypeFK());
		addColumnDefinition(new Name());
		this.att = att;
		this.btt = btt;
	}
	
		// accessors
	
	public MovementType get(String id) throws IllegalMovementTypeException{
		MovementType mt = (MovementType)find(new MovementType(id));
		if (mt == null) throw new IllegalMovementTypeException("[" + id + "]");
		return mt;
	}
	
	public Enumeration movementTypes(){
		clearConstraints();
		return records();
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached movement types=\n" + super.toString();
	}
	
	// miscellaneous
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException, IllegalCurrencyException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		AccountTypes att = new AccountTypes(con);
		BalanceTypes btt = new BalanceTypes(con);
		MovementTypes mtt = new MovementTypes(con, att, btt);
		mtt.load();
		System.out.println(mtt);
	}
	
}