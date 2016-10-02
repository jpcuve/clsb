package legacyb1;

import java.util.*;
import java.sql.*;

public class PayOuts extends DBTable{
	private Currencies ct;
	private Accounts at;
	private Movements mt;

	private class Ref extends DBString{
		public Ref(){super(true, "Reference");}
		public void set(DBRecord rec, String value){((PayOut)rec).setReference(value);}
		public String get(DBRecord rec){	return ((PayOut)rec).getReference();}
	}
	
	private class AccountFK extends DBString{
		public AccountFK(){super(false, "Account FK");}
		public void set(DBRecord rec, String value){
			try{
				Account a = at.get(value);
				((PayOut)rec).setAccount(a);
			}catch(IllegalAccountException e){
				e.printStackTrace();
				System.exit(2);
			}
		}
		public String get(DBRecord rec){	return ((PayOut)rec).getAccount().getName();}
	}
	
	private class CurrencyFK extends DBString{
		public CurrencyFK(){super(false, "Currency FK");}
		public void set(DBRecord rec, String value){
			try{
				((PayOut)rec).setCurrency(ct.get(value));
			}catch(IllegalCurrencyException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((PayOut)rec).getCurrency().getName();}
	}

	private class Amount extends DBDouble{
		public Amount(){super(false, "Amount");}
		public void set(DBRecord rec, double value){((PayOut)rec).setAmount(value);}
		public double get(DBRecord rec){	return ((PayOut)rec).getAmount();}
	}

	public PayOuts(Connection con, Currencies ct, Accounts at, Movements mt){
		super(con, "PayOuts", new PayOut());
		this.ct = ct;
		this.at = at;
		this.mt = mt;
		addColumnDefinition(new Ref());
		addColumnDefinition(new AccountFK());
		addColumnDefinition(new CurrencyFK());
		addColumnDefinition(new Amount());
	}
	
	// accessors
	
	public Enumeration payOuts() throws SQLException, DBIOException{
		clearConstraints();
		return records();
	}
	
	public Movements getMovements(){
		return this.mt;
	}
	
	// miscellaneous
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		MovementTypes mtt = new MovementTypes(con, att, btt);
		Movements mt = new Movements(con, mtt, ct, at);
		PayOuts pt = new PayOuts(con, ct, at, mt);
		for(Enumeration e = pt.payOuts(); e.hasMoreElements();){
			PayOut p = (PayOut)e.nextElement();
			System.out.println(p);
		}
	}
	
}
	