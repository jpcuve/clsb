package legacyb3.data;
 
// import java.util.*;

import legacyb3.core.ApplicationError;
import legacyb3.core.Currency;
import legacyb3.core.CurrencyPriorityIterator;
import legacyb3.core.FcttIterator;
import legacyb3.or2.DBColumn;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;
import legacyb3.or2.DBTimeOfDay;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;

public final class Currencies extends CachedServer{
	private DBColumn cCurrencyGroup;
	private DBColumn cMirrorAccount;
	
	private CurrencyGroups m_cgt;
	
	public Currencies(Connection con, CurrencyGroups cgt, Accounts at) throws SQLException{
		super(con, "Currencies", new Currency(null), "currencies");
		this.addColumnString(true, "ID", "ID");
		this.addColumnBoolean(false, "Not eligible", "NotEligible");
		this.addColumnDouble(false, "Base rate", "BaseRate");
		this.addColumnBoolean(false, "Quote", "Quote");
		this.addColumnDouble(false, "Volatility margin", "VolatilityMargin");
		this.addColumnDouble(false, "Gross split threshold", "GrossSplitThreshold");
		this.addColumnInteger(false, "Precision", "Precision");
		this.addColumnDouble(false, "Unit", "Unit");
		cCurrencyGroup = new DBColumn(this, false, CurrencyGroupFK.instance(cgt), "CurrencyGroup FK", "CurrencyGroup");
		this.addColumnDefinition(cCurrencyGroup);
		cMirrorAccount = new DBColumn(this, false, AccountFK.instance(at), "Mirror account FK", "MirrorAccount");
		this.addColumnDefinition(cMirrorAccount);
		this.addColumnDouble(false, "Balance factor", "BalanceFactor");
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "RTGS open", "RTGSOpen"));
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "RTGS close", "RTGSClose"));
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "FCTT", "FCTT"));
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "CC", "CC"));
		this.addColumnDouble(false, "Minimum pay-in", "MinimumPayIn");
		this.addColumnDouble(false, "Minimum pay-out", "MinimumPayOut");
		this.addColumnDouble(false, "Maximum pay-out", "MaximumPayOut");
		this.addColumnBoolean(false, "Suspended for pay-out", "SuspendedForPayOut");
		m_cgt = cgt;
		this.checkStructure();
	}
	
	// overloaded.
	
	public void validate(DBRecord rec) throws SQLException{
		Currency ccy = (Currency)rec;
		if(ccy.getRTGSOpen() == null || ccy.getRTGSClose() == null || ccy.getFCTT() == null || ccy.getCC() == null) throw new SQLException(ccy.getKey() + ApplicationError.isNull );
		if(ccy.getBaseRate() <= 0) throw new SQLException(ccy.getKey() + " base rate" + ApplicationError.isNegativeOrZero );
		if(ccy.getVolatilityMargin() < 0) throw new SQLException(ccy.getKey() + " volatility margin" + ApplicationError.isNegative );
		if(ccy.getGrossSplitThreshold() <= 0) throw new SQLException(ccy.getKey() + " gross split threshold" + ApplicationError.isNegativeOrZero );
		if(ccy.getUnit() <= 0) throw new SQLException(ccy.getKey() + " payment unit" + ApplicationError.isNegativeOrZero );
		if(ccy.getBalanceFactor() <= 0) throw new SQLException(ccy.getKey() + " balance factor" + ApplicationError.isNegativeOrZero );
		if(ccy.getMinimumPayIn() < 0) throw new SQLException(ccy.getKey() + " minimum pay-in" + ApplicationError.isNegative );
		if(ccy.getMaximumPayOut() < 0) throw new SQLException(ccy.getKey() + " maximum pay-out" + ApplicationError.isNegative );
		if(ccy.getMinimumPayOut() < 0) throw new SQLException(ccy.getKey() + " minimum pay-out" + ApplicationError.isNegative );
		if(ccy.getRTGSClose().isBefore(ccy.getRTGSOpen())) throw new SQLException(ccy.getKey() + " RTGS open" + ApplicationError.isAfter + " RTGS close" );
		if(ccy.getFCTT().isBefore(ccy.getRTGSOpen()) || ccy.getFCTT().isAfter(ccy.getRTGSClose())) throw new SQLException(ccy.getKey() + " FCTT" + ApplicationError.isOutsideRTGSOpenAndClose );
		if(ccy.getCC().isBefore(ccy.getRTGSOpen()) || ccy.getCC().isAfter(ccy.getRTGSClose())) throw new SQLException(ccy.getKey() + " CC" + ApplicationError.isOutsideRTGSOpenAndClose);
		if(ccy.getFCTT().isAfter(ccy.getCC())) throw new SQLException(ccy.getKey() + " FCTT" + ApplicationError.isAfter + " CC");
		ccy.getCurrencyGroup().addCurrency(ccy);
	}
		
	// accessors
	
	public Currency get(String name) throws SQLException{
		Currency c = (Currency)find(new Currency(name));
		if (c == null) throw new SQLException("Currency [" + name + "]" + ApplicationError.notFound);
		return c;
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached currencies=\n" + super.toString();
	}
	
	// miscellaneous
	
	public Currency getBase() throws SQLException{
		for(DBEnumeration e = currencies(); e.hasMoreElements();){
			Currency c = (Currency)e.nextElement();
			if (c.getRate() == (float)1.0) return c;
		}
		return null;
	}
	
	public DBEnumeration currencies() throws SQLException{
		return records();
	}
	
	public Enumeration currenciesPerLiquidity(boolean ascending) throws SQLException{
		return new CurrencyPriorityIterator(this, m_cgt, ascending);
	}

	public Iterator fctts(boolean ascending) throws SQLException{
		return new FcttIterator(this, ascending);
	}

}