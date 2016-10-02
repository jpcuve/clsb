package legacyb1;

import java.util.*;
import java.sql.*;

public class Movements extends DBTable{
	private MovementTypes mtt;
	private Currencies ct;
	private Accounts at;
	public static Movements mt;
	
	private class ParentRef extends DBString{
		public ParentRef(){super(false, "Parent Reference");}
		public void set(DBRecord rec, String value){((Movement)rec).setParentReference(value);}
		public String get(DBRecord rec){	return ((Movement)rec).getParentReference();}
	}
	
	private class MovementTypeFK extends DBString{
		public MovementTypeFK(){super(false, "Movement type FK");}
		public void set(DBRecord rec, String value){
			try{
				MovementType mt = mtt.get(value);
				((Movement)rec).setMovementType(mt);
			}catch(IllegalMovementTypeException e){
				e.printStackTrace();
				System.exit(2);
			}
		}
		public String get(DBRecord rec){	return ((Movement)rec).getMovementType().getID();}
	}	
	
	private class AccountDBFK extends DBString{
		public AccountDBFK(){super(false, "Account DB FK");}
		public void set(DBRecord rec, String value){
			try{
				Account a = at.get(value);
				((Movement)rec).setDBAccount(a);
			}catch(IllegalAccountException e){
				e.printStackTrace();
				System.exit(2);
			}
		}
		public String get(DBRecord rec){	return ((Movement)rec).getDBAccount().getName();}
	}
	
	private class AccountCRFK extends DBString{
		public AccountCRFK(){super(false, "Account CR FK");}
		public void set(DBRecord rec, String value){
			try{
				Account a = at.get(value);
				((Movement)rec).setCRAccount(a);
			}catch(IllegalAccountException e){
				e.printStackTrace();
				System.exit(2);
			}
		}
		public String get(DBRecord rec){	return ((Movement)rec).getCRAccount().getName();}
	}
	
	private class CurrencyFK extends DBString{
		public CurrencyFK(){super(true, "Currency FK");}
		public void set(DBRecord rec, String value){
			try{
				((Movement)rec).setCurrency(ct.get(value));
			}catch(IllegalCurrencyException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((Movement)rec).getCurrency().getName();}
	}

	private class Amount extends DBDouble{
		public Amount(){super(false, "Amount");}
		public void set(DBRecord rec, double value){((Movement)rec).setAmount(value);}
		public double get(DBRecord rec){	return ((Movement)rec).getAmount();}
	}

	public Movements(Connection con, MovementTypes mtt, Currencies ct, Accounts at){
		super(con, "Movements", new Movement());
		this.ct = ct;
		this.at = at;
		this.mtt = mtt;
		addColumnDefinition(new MovementTypeFK());
		addColumnDefinition(new ParentRef());
		addColumnDefinition(new AccountDBFK());
		addColumnDefinition(new AccountCRFK());
		addColumnDefinition(new CurrencyFK());
		addColumnDefinition(new Amount());
	}
	
	public Enumeration movements() throws SQLException, DBIOException{
		clearConstraints();
		return records();
	}
	
	// miscellaneous
	
	public void book(Settlable s) throws SQLException, DBIOException{
		MovementType settle = null;
		MovementType proj = null;
		try{
			settle = mtt.get("SE");
		}catch(IllegalMovementTypeException ex){
			ex.printStackTrace();
			System.exit(2);
		}
		for(Enumeration e1 = s.movements(settle); e1.hasMoreElements();){
			Movement m = (Movement)e1.nextElement();
			try{
				m.book();
				insert(m);
			}catch(IllegalMovementException ex){
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public void book(PayOut p) throws SQLException, DBIOException{
		MovementType po = null;
		try{
			po = mtt.get("PO");
		}catch(IllegalMovementTypeException ex){
			ex.printStackTrace();
			System.exit(2);
		}
		Currency c = p.getCurrency();
		Movement m = new Movement(p.getReference(), po, p.getAccount(), c.getMirrorAccount(), c, p.getAmount());
		try{
			m.book();
			insert(m);
		}catch(IllegalMovementException ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void bookOut(PayOut p, boolean dir)  throws SQLException, DBIOException{
		MovementType ou = null;
		try{
			ou = mtt.get("OU");
		}catch(IllegalMovementTypeException ex){
			ex.printStackTrace();
			System.exit(2);
		}
		Currency c = p.getCurrency();
		Movement m = new Movement(p.getReference(), ou, c.getMirrorAccount(), p.getAccount(), c, p.getAmount());
		if (dir == false) m.inv();
		try{
			m.book();
		}catch(IllegalMovementException ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}

	
	public void bookProjection(Settlable s, boolean dir) throws SQLException, DBIOException{
		MovementType proj = null;
		try{
			proj = mtt.get("PR");
		}catch(IllegalMovementTypeException ex){
			ex.printStackTrace();
			System.exit(2);
		}
		for(Enumeration e1 = s.movements(proj); e1.hasMoreElements();){
			Movement m = (Movement)e1.nextElement();
			if(dir == false) m.inv();
			try{
				m.book();
				insert(m);
			}catch(IllegalMovementException ex){
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

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
		for(Enumeration e = mt.movements(); e.hasMoreElements();){
			Movement m = (Movement)e.nextElement();
			System.out.println(m);
		}
	}
	
}
	