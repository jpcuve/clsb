package legacyb1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class Accounts extends DBCachedTable{
	private Currencies ct;
	private AccountTypes att;
	private CLSBank B;
	
	// inner classes
	
	private class Name extends DBString{
		public Name(){super(true, "Account");}
		public void set(DBRecord rec, String value){((Account)rec).setName(value);}
		public String get(DBRecord rec){	return ((Account)rec).getName();}
	}
	
	private class AccountTypeFK extends DBString{
		private AccountType cbAcTyp;
		public AccountTypeFK(){	super(false, "Account type FK"); }
		public void set(DBRecord rec, String value){
			try{
				Account a = (Account)rec;
				AccountType at = (AccountType)att.get(value);
				a.setAccountType(at);
				at.setAccount(a);
			}catch(IllegalAccountTypeException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((Account)rec).getAccountType().getID();}
	}
	
	private class ASPL extends DBDouble {
		public ASPL(){super(false, "Aspl");}
		public void set(DBRecord rec, double value){((Account)rec).setASPL(value);}
		public double get(DBRecord rec){	return ((Account)rec).getASPL();}
	}
	
	private class NonCashCollateral extends DBDouble {
		public NonCashCollateral(){super(false, "Non-cash collateral");}
		public void set(DBRecord rec, double value){((Account)rec).setNonCashCollateral(value);}
		public double get(DBRecord rec){	return ((Account)rec).getNonCashCollateral();}
	}
	
	// constructors
	
	public Accounts(Connection con, AccountTypes att, CLSBank B) throws SQLException, DBIOException{
		super(con, "Accounts", new Account("Dummy"));
		addColumnDefinition(new Name());
		addColumnDefinition(new AccountTypeFK());
		addColumnDefinition(new ASPL());
		addColumnDefinition(new NonCashCollateral());
		this.att = att;
		this.B = B;
	}



    // accessors
	
	public Account get(String name) throws IllegalAccountException{
		Account a = (Account)find(new Account(name));
		if (a == null) throw new IllegalAccountException();
		return a;
	}
	
	public Enumeration accounts(){
		clearConstraints();
		return records();
	}
	
	public Enumeration accounts(String sActyp){
		clearConstraints();
		getColumnDefinition("Account type FK").setConstraintEqual(sActyp);
		return records();
	}
	
	public Enumeration accountsPerAvailablePayOut(){
		return new AccountIterator(this);
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached accounts=\n" + super.toString();
	}
	
	public void printAll(){
		for(Enumeration e1 = accounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			System.out.println(a);
			for(Enumeration e2 = a.balances(); e2.hasMoreElements();){
				Balance b = (Balance)e2.nextElement();
				System.out.println("  " + b);
			}
		}
	}
	
	// debug
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		Balances bt = new Balances(con, at, btt, ct);
		for(Enumeration e1 = at.accounts(); e1.hasMoreElements();) System.out.println(e1.nextElement());
		System.out.println(at);
	}

}