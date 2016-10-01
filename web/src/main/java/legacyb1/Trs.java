package legacyb1;

import java.util.*;
import java.sql.*;

public class Trs extends DBTable{
	private Currencies ct;
	private Accounts at;
	private Legs lt;
	private Movements mt;
	private String legsTableName;
	
	// inner classes
	
	private class PolyTr extends Tr{
		private boolean lcf;
		public PolyTr(String ref, String parentRef, Account a1, Account a2, String status, boolean lcf) throws IllegalAccountException{
			super(ref, parentRef, a1, a2, status);
			this.lcf = lcf;
		}
		public PolyTr(String ref) throws IllegalAccountException{
			super(ref, null, new Account(null), new Account(null), null);
			this.lcf = false;
		}
		public void setLCF(boolean lcf){
			this.lcf = lcf;
		}
		public boolean getLCF(){
			return lcf;
		}
		public Object clone(){
			PolyTr str = null;
			try{
				str = new PolyTr(getReference(), getParentReference(), getFirstAccount(), getSecondAccount(), getStatus(), lcf);
			}catch(IllegalAccountException e){
			}
			return str;
		}
		public String toString(){
			return "[ref=" + getReference() + "parent=" + getParentReference() + ", a1=" + getFirstAccount().getName() + ", a2=" + getSecondAccount().getName() + ", lcf=" + lcf + ", st=" + getStatus() + "]";
		}
	}
	
	private class PolyTrIterator extends DBRecordIterator{
		DBTable tt;
		public PolyTrIterator(DBTable tt) throws SQLException, DBIOException{
			super(tt);
			this.tt = tt;
		}
		public Object nextElement(){
			PolyTr str = (PolyTr)super.nextElement();
			Tr t = null;
			try{
				if (str.getLCF()){
					t = new LinkedCashflowTransaction(str.getReference(), str.getParentReference(), str.getFirstAccount(), str.getSecondAccount(), str.getStatus());
				}else{
					t = new GrossTransaction(str.getReference(), str.getParentReference(), str.getFirstAccount(), str.getSecondAccount(), str.getStatus());
				}
				t.setTable(tt);
				t.setQueried();
				lt.getColumnDefinition("Tr FK").setConstraintEqual(str.getReference());
				for(Enumeration e = lt.records(); e.hasMoreElements();){
					Leg l = (Leg)e.nextElement();
					l.setTr(t);
					t.net(l);
				}
			}catch(Throwable e){
				e.printStackTrace();
				System.exit(2);
			}
			return t;
		}
	}
	
	private class Legs extends DBTable{
			
		private class TrFK extends DBString{
			public TrFK(){super(true, "Tr FK");}
			public void set(DBRecord rec, String value){}
			public String get(DBRecord rec){	return ((Leg)rec).getTr().getReference();}
		}
	
		
		private class CurrencyFK extends DBString{
			public CurrencyFK(){super(true, "Currency FK");}
			public void set(DBRecord rec, String value){
				try{
					((Leg)rec).setCurrency(ct.get(value));
				}catch(IllegalCurrencyException e){
					e.printStackTrace();
					System.exit(1);
				}
			}
			public String get(DBRecord rec){	return ((Leg)rec).getCurrency().getName();}
		}
		
		private class Dir extends DBBoolean{
			public Dir(){super(false, "1 to 2");}
			public void set(DBRecord rec, boolean value){((Leg)rec).setDir(value);}
			public boolean get(DBRecord rec){	return ((Leg)rec).getDir();}
		}
		
		private class Amount extends DBDouble{
			public Amount(){super(false, "Amount");}
			public void set(DBRecord rec, double value){((Leg)rec).setAmount(value);}
			public double get(DBRecord rec){	return ((Leg)rec).getAmount();}
		}

		public Legs(Connection con, String tableName, Currencies ct){
			super(con, tableName, new Leg(null, true, null, 0));
			addColumnDefinition(new TrFK());
			addColumnDefinition(new CurrencyFK());
			addColumnDefinition(new Dir());
			addColumnDefinition(new Amount());
		}
	}
	
	private class Reference extends DBString{
		public Reference(){super(true, "Reference");}
		public void set(DBRecord rec, String value){((PolyTr)rec).setReference(value);}
		public String get(DBRecord rec){	return ((PolyTr)rec).getReference();}
	}
	
	private class ParentReference extends DBString{
		public ParentReference(){super(false, "Parent Reference");}
		public void set(DBRecord rec, String value){((PolyTr)rec).setParentReference(value);}
		public String get(DBRecord rec){	return ((PolyTr)rec).getParentReference();}
	}
	
	private class Account1FK extends DBString{
		public Account1FK(){super(false, "Account 1 FK");}
		public void set(DBRecord rec, String value){
			try{
				Account a = at.get(value);
				((PolyTr)rec).setFirstAccount(a);
			}catch(IllegalAccountException e){
				e.printStackTrace();
				System.exit(2);
			}
		}
		public String get(DBRecord rec){	return ((PolyTr)rec).getFirstAccount().getName();}
	}
	
	private class Account2FK extends DBString{
		public Account2FK(){super(false, "Account 2 FK");}
		public void set(DBRecord rec, String value){
			try{
				Account a = at.get(value);
				((PolyTr)rec).setSecondAccount(a);
			}catch(IllegalAccountException e){
				e.printStackTrace();
				System.exit(2);
			}
		}
		public String get(DBRecord rec){	return ((PolyTr)rec).getSecondAccount().getName();}
	}
	
	private class LCF extends DBBoolean{
		public LCF(){super(false, "Linked cash flow");}
		public void set(DBRecord rec, boolean value){((PolyTr)rec).setLCF(value);}
		public boolean get(DBRecord rec){	return ((PolyTr)rec).getLCF();}
	}
	
	private class Status extends DBString{
		public Status(){super(false, "Status");}
		public void set(DBRecord rec, String value){((PolyTr)rec).setStatus(value);}
		public String get(DBRecord rec){	return ((PolyTr)rec).getStatus();}
	}
	
	// constructor
	
	public Trs(Connection con, String tableName, String legsTableName, Currencies ct, Accounts at, Movements mt){
		super(con, tableName);
		try{
			setPattern(new PolyTr(null));
		}catch(IllegalAccountException e){
			e.printStackTrace();
			System.exit(1);
		}
		this.ct = ct;
		this.at = at;
		this.mt = mt;
		this.legsTableName = legsTableName;
		addColumnDefinition(new Reference());
		addColumnDefinition(new ParentReference());
		addColumnDefinition(new Account1FK());
		addColumnDefinition(new Account2FK());
		addColumnDefinition(new LCF());
		addColumnDefinition(new Status());
		lt = new Legs(con, legsTableName, ct);
	}
	
	// accessors
	
	public String getLegsTableName(){
		return legsTableName;
	}
	
	public Accounts getAccounts(){
		return at;
	}
	
	public Currencies getCurrencies(){
		return ct;
	}
	
	public Movements getMovements(){
		return mt;
	}

	public Tr get(String name) throws IllegalTrException, IllegalAccountException, TooManyLegsException, DBIOException, SQLException{
		PolyTr str = new PolyTr(name);
		if (!super.query(str)) throw new IllegalTrException();
		Tr t = null;
		if (str.getLCF()){
			t = new LinkedCashflowTransaction(str.getReference(), str.getParentReference(), str.getFirstAccount(), str.getSecondAccount(), str.getStatus());
		}else{
			t = new GrossTransaction(str.getReference(), str.getParentReference(), str.getFirstAccount(), str.getSecondAccount(), str.getStatus());
		}
		t.setTable(this);
		lt.getColumnDefinition("Tr FK").setConstraintEqual(str.getReference());
		for(Enumeration e = lt.records(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			l.setTr(t);
			t.net(l);
		}
		return t;
	}
	
	// miscellaneous
	

	public boolean query(DBRecord rec) throws DBIOException, SQLException{
		Tr tr = (Tr)rec;
		boolean ret = false;
		try{
			PolyTr str = new PolyTr(tr.getReference());
			ret = super.query(str);
			tr.setReference(str.getReference());
			tr.setParentReference(str.getParentReference());
			tr.setFirstAccount(str.getFirstAccount());
			tr.setSecondAccount(str.getSecondAccount());
			tr.setStatus(str.getStatus());
			tr.setTable(this);
			lt.getColumnDefinition("Tr FK").setConstraintEqual(str.getReference());
			for(Enumeration e = lt.records(); e.hasMoreElements();){
				Leg l = (Leg)e.nextElement();
				l.setTr(tr);
				tr.net(l);
			}
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
		return ret;
	}

	public void update(DBRecord rec) throws DBIOException, SQLException{
		Tr tr = (Tr)rec;
		try{
			PolyTr str = new PolyTr(tr.getReference(), tr.getParentReference(), tr.getFirstAccount(), tr.getSecondAccount(), tr.getStatus(), tr.getClass().getName().equals("LinkedCashflowTransaction"));
			super.update(str);
		}catch(IllegalAccountException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void insert(DBRecord rec) throws DBIOException, SQLException{
		Tr tr = (Tr)rec;
		try{
			PolyTr str = new PolyTr(tr.getReference(), tr.getParentReference(), tr.getFirstAccount(), tr.getSecondAccount(), tr.getStatus(), tr.getClass().getName().equals("LinkedCashflowTransaction"));
			super.insert(str);
			for(Enumeration e = tr.legs(); e.hasMoreElements();){
				Leg l = (Leg)e.nextElement();
				lt.insert(l);
			}
		}catch(IllegalAccountException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Enumeration transactions(){
		Enumeration e = null;
		try{
			e = new PolyTrIterator(this);
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
		return e;
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
		Trs tt = new Trs(con, "Trs", "TrLegs", ct, at, mt);
		for(Enumeration e = tt.transactions(); e.hasMoreElements();){
			Tr t = (Tr)e.nextElement();
			System.out.println(t);
		}
	}

}