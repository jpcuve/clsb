package legacyb1;

import java.util.*;
import java.sql.*;

public class PayInSchedules extends DBCachedTable{
	private Accounts at;
	private Currencies ct;
	
	// inner classes
	
	private class AccountFK extends DBString{
		public AccountFK(){super(true, "Account FK");}
		public void set(DBRecord rec, String value){
			try{
				((PayInSchedule)rec).setAccount(at.get(value));
				linkAccount(rec);
			}catch(IllegalAccountException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((PayInSchedule)rec).getAccount().getName();}
	}
	
	private class CurrencyFK extends DBString{
		public CurrencyFK(){super(true, "Currency FK");}
		public void set(DBRecord rec, String value){
			try{
				((PayInSchedule)rec).setCurrency(ct.get(value));
				linkAccount(rec);
			}catch(IllegalCurrencyException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((PayInSchedule)rec).getCurrency().getName();}
	}
	
	private class Time extends DBTimeOfDay{
		public Time(){super(true, "Time");}
		public void set(DBRecord rec, TimeOfDay value){
			((PayInSchedule)rec).setTime(value);
			linkAccount(rec);
		}
		public TimeOfDay get(DBRecord rec){	return ((PayInSchedule)rec).getTime();}
	}

	private class Amount extends DBDouble{
		public Amount(){super(false, "Amount");}
		public void set(DBRecord rec, double value){
			((PayInSchedule)rec).setAmount(value);
			linkAccount(rec);
		}
		public double get(DBRecord rec){	return ((PayInSchedule)rec).getAmount();}
	}
	
	private void linkAccount(DBRecord rec){
		PayInSchedule pis = (PayInSchedule)rec;
		if(pis.getAccount() != null && pis.getCurrency() != null && pis.getTime() != null && pis.getAmount() != 0){
			pis.getAccount().setPayInScheduleFlow(pis);
		}
	}
	
	// constructor

	public PayInSchedules(Connection con, Accounts at, Currencies ct) throws SQLException{
		super(con, "PayInSchedules", new PayInSchedule());
		this.at = at;
		this.ct = ct;
		addColumnDefinition(new CurrencyFK());
		addColumnDefinition(new AccountFK());
		addColumnDefinition(new Time());
		addColumnDefinition(new Amount());
	}
	
	// accessors
	
	public Enumeration payInSchedules(){
		clearConstraints();
		return records();
	}
		
	// JAVA support
	
	public String toString(){
		return "Cached pay-in schedules=\n" + super.toString();
	}
	
	// miscellaneous
		
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException, IllegalCurrencyException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		PayInSchedules pist = new PayInSchedules(con, at, ct);
		pist.load();
		System.out.println(pist);
		for(Enumeration e1 = at.accounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			for(Enumeration e2 = ct.currencies(); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				System.out.println("Ac=" + a.getName() + ", ccy=" + c.getName());
				CashFlow cf = a.getPayInSchedule(c);
				if(cf != null) System.out.println(cf);
			}
		}
	}
	
}