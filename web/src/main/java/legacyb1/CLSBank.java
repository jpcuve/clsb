package legacyb1;

import java.sql.*;

public class CLSBank extends DBCachedTable{
	private Clsb clsb;
	
	// inner classes
	private class SCTT extends DBTimeOfDay{
		public SCTT(){super(false, "SCTT");}
		public void set(DBRecord rec, TimeOfDay value){((Clsb)rec).setSCTT(value);}
		public TimeOfDay get(DBRecord rec){	return ((Clsb)rec).getSCTT();}
	}
	
	private class MarginMultiplier extends DBDouble{
		public MarginMultiplier(){super(false, "Margin Multiplier");}
		public void set(DBRecord rec, double value){((Clsb)rec).setMarginMultiplier(value);}
		public double get(DBRecord rec){	return ((Clsb)rec).getMarginMultiplier();}
	}
	
	private class PayMultiplier extends DBDouble{
		public PayMultiplier(){super(false, "Pay Multiplier");}
		public void set(DBRecord rec, double value){((Clsb)rec).setPayMultiplier(value);}
		public double get(DBRecord rec){	return ((Clsb)rec).getPayMultiplier();}
	}
	
	private class CoverMultiplier extends DBDouble{
		public CoverMultiplier(){super(false, "Cover Multiplier");}
		public void set(DBRecord rec, double value){((Clsb)rec).setCoverMultiplier(value);}
		public double get(DBRecord rec){	return ((Clsb)rec).getCoverMultiplier();}
	}
	
	private class Opening extends DBTimeOfDay{
		public Opening(){super(false, "Opening");}
		public void set(DBRecord rec, TimeOfDay value){((Clsb)rec).setOpening(value);}
		public TimeOfDay get(DBRecord rec){	return ((Clsb)rec).getOpening();}
	}
	
	private class Closing extends DBTimeOfDay{
		public Closing(){super(false, "Closing");}
		public void set(DBRecord rec, TimeOfDay value){((Clsb)rec).setClosing(value);}
		public TimeOfDay get(DBRecord rec){	return ((Clsb)rec).getClosing();}
	}
	
	private class Current extends DBTimeOfDay{
		public Current(){super(false, "Current");}
		public void set(DBRecord rec, TimeOfDay value){((Clsb)rec).setCurrent(value);}
		public TimeOfDay get(DBRecord rec){	return ((Clsb)rec).getCurrent();}
	}
	
	
	public CLSBank(Connection con) throws SQLException, DBIOException{
		super(con, "CLSBank", new Clsb());
		addColumnDefinition(new SCTT());
		addColumnDefinition(new MarginMultiplier());
		addColumnDefinition(new PayMultiplier());
		addColumnDefinition(new CoverMultiplier());
		addColumnDefinition(new Opening());
		addColumnDefinition(new Closing());
		addColumnDefinition(new Current());
		clsb = (Clsb)find(new Clsb());
	}
	
	// accessors
	
	public Clsb get(){
		return clsb;
	}
	
	// miscellaneous
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, DBIOException, IllegalCurrencyException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank c= new CLSBank(con);
		c.load();
		Clsb clsb = c.get();
		System.out.println(clsb);
	}


}