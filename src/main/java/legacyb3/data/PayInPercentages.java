package legacyb3.data;

import legacyb3.core.ApplicationError;
import legacyb3.core.PayInPercentage;
import legacyb3.or2.DBColumn;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;

import java.sql.Connection;
import java.sql.SQLException;

public class PayInPercentages extends CachedServer {
	private DBColumn m_cCurrency;
	private DBColumn m_cPayInDeadline;
	
	// constructor

	public PayInPercentages(Connection con, Currencies ct, PayInDeadlines pidt) throws SQLException{
		super(con, "PayInPercentages", new PayInPercentage(), "pay-in percentages");
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		m_cPayInDeadline = new DBColumn(this, true, PayInDeadlineFK.instance(pidt), "PayInDeadline FK", "PayInDeadline");
		this.addColumnDefinition(m_cPayInDeadline);
		this.addColumnDouble(false, "Percentage", "Percentage");
		this.checkStructure();
	}
	
	// overloaded. Linking pay-in schedule with its account.
	
	public void validate(DBRecord rec) throws SQLException {
		PayInPercentage pip = (PayInPercentage)rec;
		pip.getCurrency().setPayInPercentage(pip);
		if(pip.getPercentage() < 0) throw new SQLException(pip.getKey() + " percentage" + ApplicationError.isNegative );
	}
	
	public DBEnumeration payInPercentages() throws SQLException{
		return records();
	}
		
	// JAVA support
	
	public String toString(){
		return "Cached pay-in percentages=\n" + super.toString();
	}
			
}
