package legacyb1;

import java.util.*;
import java.sql.*;

public class Currencies extends DBCachedTable{
	private CurrencyGroups cgt;
	private Accounts at;
	
	// inner classes
	private class ID extends DBString{
		public ID(){super(true, "ID");}
		public void set(DBRecord rec, String value){((Currency)rec).setName(value);}
		public String get(DBRecord rec){	return ((Currency)rec).getName();}
	}
	private class BaseRate extends DBDouble{
		public BaseRate(){super(false, "Base rate");}
		public void set(DBRecord rec, double value){((Currency)rec).setBaseRate(value);}
		public double get(DBRecord rec){	return ((Currency)rec).getBaseRate();}
	}
	private class Quote extends DBBoolean{
		public Quote(){super(false, "Quote");}
		public void set(DBRecord rec, boolean value){((Currency)rec).setQuote(value);}
		public boolean get(DBRecord rec){	return ((Currency)rec).getQuote();}
	}
	private class VolatilityMargin extends DBDouble{
		public VolatilityMargin(){super(false, "Volatility margin");}
		public void set(DBRecord rec, double value){((Currency)rec).setVolatilityMargin(value);}
		public double get(DBRecord rec){	return ((Currency)rec).getVolatilityMargin();}
	}
	private class GrossSplitThreshold extends DBDouble{
		public GrossSplitThreshold(){super(false, "Gross split threshold");}
		public void set(DBRecord rec, double value){((Currency)rec).setGrossSplitThreshold(value);}
		public double get(DBRecord rec){	return ((Currency)rec).getGrossSplitThreshold();}
	}
	private class Precision extends DBInteger{
		public Precision(){super(false, "Precision");}
		public void set(DBRecord rec, int value){((Currency)rec).setPrecision(value);}
		public int get(DBRecord rec){	return ((Currency)rec).getPrecision();}
	}
	private class CurrencyGroupFK extends DBString{
		public CurrencyGroupFK(){super(false, "CurrencyGroup FK");}
		public void set(DBRecord rec, String value){
			try{
				CurrencyGroup cg = cgt.get(value);
				Currency c = (Currency)rec;
				c.setCurrencyGroup(cg);
				cg.setCurrency(c);
			}catch(IllegalCurrencyGroupException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((Currency)rec).getCurrencyGroup().getName();}
	}
	private class MirrorAccountFK extends DBString{
		public MirrorAccountFK(){super(false, "Mirror account FK");}
		public void set(DBRecord rec, String value){
			try{
				((Currency)rec).setMirrorAccount(at.get(value));
			}catch(IllegalAccountException ex){
				ex.printStackTrace();
				System.exit(1);
			}
		}
		public String get(DBRecord rec){	return ((Currency)rec).getMirrorAccount().getName();}
	}
	private class BalanceFactor extends DBDouble{
		public BalanceFactor(){super(false, "Balance factor");}
		public void set(DBRecord rec, double value){((Currency)rec).setBalanceFactor(value);}
		public double get(DBRecord rec){	return ((Currency)rec).getBalanceFactor();}
	}
	private class RTGSOpen extends DBTimeOfDay{
		public RTGSOpen(){super(false, "RTGS open");}
		public void set(DBRecord rec, TimeOfDay value){((Currency)rec).setRTGSOpen(value);}
		public TimeOfDay get(DBRecord rec){	return ((Currency)rec).getRTGSOpen();}
	}
	private class RTGSClose extends DBTimeOfDay{
		public RTGSClose(){super(false, "RTGS close");}
		public void set(DBRecord rec, TimeOfDay value){((Currency)rec).setRTGSClose(value);}
		public TimeOfDay get(DBRecord rec){	return ((Currency)rec).getRTGSClose();}
	}
	private class FCTT extends DBTimeOfDay{
		public FCTT(){super(false, "FCTT");}
		public void set(DBRecord rec, TimeOfDay value){((Currency)rec).setFCTT(value);}
		public TimeOfDay get(DBRecord rec){	return ((Currency)rec).getFCTT();}
	}
	private class CC extends DBTimeOfDay{
		public CC(){super(false, "CC");}
		public void set(DBRecord rec, TimeOfDay value){((Currency)rec).setCC(value);}
		public TimeOfDay get(DBRecord rec){	return ((Currency)rec).getCC();}
	}
	private class MinimumPayOut extends DBDouble{
		public MinimumPayOut(){super(false, "Minimum pay-out");}
		public void set(DBRecord rec, double value){((Currency)rec).setMinimumPayOut(value);}
		public double get(DBRecord rec){	return ((Currency)rec).getMinimumPayOut();}
	}
	private class MaximumPayOut extends DBDouble{
		public MaximumPayOut(){super(false, "Maximum pay-out");}
		public void set(DBRecord rec, double value){((Currency)rec).setMaximumPayOut(value);}
		public double get(DBRecord rec){	return ((Currency)rec).getMaximumPayOut();}
	}

	// constructors
	
	public Currencies(Connection con, CurrencyGroups cgt, Accounts at) throws SQLException{
		super(con, "Currencies", new Currency(null));
		addColumnDefinition(new ID());
		addColumnDefinition(new BaseRate());
		addColumnDefinition(new Quote());
		addColumnDefinition(new VolatilityMargin());
		addColumnDefinition(new GrossSplitThreshold());
		addColumnDefinition(new Precision());
		addColumnDefinition(new CurrencyGroupFK());
		addColumnDefinition(new MirrorAccountFK());
		addColumnDefinition(new BalanceFactor());
		addColumnDefinition(new RTGSOpen());
		addColumnDefinition(new RTGSClose());
		addColumnDefinition(new FCTT());
		addColumnDefinition(new CC());
		addColumnDefinition(new MinimumPayOut());
		addColumnDefinition(new MaximumPayOut());
		this.cgt = cgt;
		this.at = at;
	}
	
	// accessors
	
	public Currency get(String name) throws IllegalCurrencyException{
		Currency c = (Currency)find(new Currency(name));
		if (c == null) throw new IllegalCurrencyException("[" + name + "]");
		return c;
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached currencies=\n" + super.toString();
	}
	
	// miscellaneous
	
	public Currency getBase(){
		for(Enumeration e = currencies(); e.hasMoreElements();){
			Currency c = (Currency)e.nextElement();
			if (c.getRate() == (float)1.0) return c;
		}
		return null;
	}
	
	public Enumeration currencies(){
		clearConstraints();
		return records();
	}
	
	public Enumeration currenciesPerLiquidity(boolean ascending){
		return new CurrencyPriorityIterator(this, cgt, ascending);
	}

	public Enumeration currenciesPerFCTT(boolean ascending){
		return new CurrencyFCTTIterator(this, ascending);
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
		ct.load();
		System.out.println(cgt);
		System.out.println(ct);
		System.out.println("Currency Groups");
		for(Enumeration e1 = cgt.currencyGroups(); e1.hasMoreElements();){
			CurrencyGroup cg = (CurrencyGroup)e1.nextElement();
			System.out.println(cg);
			for(Enumeration e2 = cg.currencies(); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				System.out.println("  "+ c);
			}
		}	
		System.out.println("In order of liquidity:");
		for(Enumeration e = ct.currenciesPerLiquidity(true); e.hasMoreElements();){
			System.out.println((Currency)e.nextElement());
		}
	}

}