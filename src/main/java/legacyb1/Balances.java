package legacyb1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class Balances extends DBCachedTable{
	private Accounts at;
	private BalanceTypes btt;
	private Currencies ct;
	
	// inner classes
	
	private class AccountFK extends DBString{
		public AccountFK(){super(true, "Account FK");}
		public void set(DBRecord rec, String value){
			try{
				((Balance)rec).setAccount(at.get(value));
				linkAccount(rec);
			}catch(IllegalAccountException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((Balance)rec).getAccount().getName();}
	}
	
	private class BalanceTypeFK extends DBString{
		public BalanceTypeFK(){super(true, "Balance type FK");}
		public void set(DBRecord rec, String value){
			try{
				((Balance)rec).setBalanceType(btt.get(value));
				linkAccount(rec);
		}catch(IllegalBalanceTypeException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((Balance)rec).getBalanceType().getID();}
	}
	
	private class CurrencyFK extends DBString{
		public CurrencyFK(){super(true, "Currency FK");}
		public void set(DBRecord rec, String value){
			try{
				((Balance)rec).setCurrency(ct.get(value));
				linkAccount(rec);
			}catch(IllegalCurrencyException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((Balance)rec).getCurrency().getName();}
	}
	
	private class Bal extends DBDouble {
		public Bal(){super(false, "Balance");}
		public void set(DBRecord rec, double value){((Balance)rec).setAmount(value);}
		public double get(DBRecord rec){	return ((Balance)rec).getAmount();}
	}
	
	private void linkAccount(DBRecord rec){
		Balance b = (Balance)rec;
		if(b.getAccount() != null && b.getBalanceType() != null && b.getCurrency() != null){
			b.getAccount().setBalance(b);
			b.getBalanceType().setBalance(b);
		}
	}
	
	// constructor

	public Balances(Connection con, Accounts at, BalanceTypes btt, Currencies ct) throws SQLException{
		super(con, "Balances", new Balance(), 5.0); // make sure cache is BIG
		this.at = at;
		this.btt = btt;
		this.ct = ct;
		addColumnDefinition(new CurrencyFK());
		addColumnDefinition(new AccountFK());
		addColumnDefinition(new BalanceTypeFK());
		addColumnDefinition(new Bal());
	}
	
	// accessors
	
	public Enumeration balances(){
		clearConstraints();
		return records();
	}
	
	public Enumeration balances(BalanceType btyp){
		getColumnDefinition("Balance type FK").setConstraintEqual(btyp.getID());
		return records();
	}
		
	// JAVA support
	
	public String toString(){
		return "Cached balances=\n" + super.toString();
	}
	
	// miscellaneous
		
	public void save(){
		for(Enumeration e1 = at.accounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			for(Enumeration e2 = a.balances(); e2.hasMoreElements();){
				Balance b = (Balance)e2.nextElement();
				if(b.getTable() == null) b.setTable(this);
				save(b);
			}
		}
		super.save();
	}
				
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException, IllegalCurrencyException {
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		Balances bt = new Balances(con, at, btt, ct);
		bt.load();
		System.out.println(bt);
		for(Enumeration e = at.accounts(); e.hasMoreElements();){
			Account a = (Account)e.nextElement();
			a.printAll();
		}
	}
	
}