package legacyb3.data;

import legacyb3.core.ApplicationError;
import legacyb3.core.PayInDeadline;
import legacyb3.or2.DBColumn;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;
import legacyb3.or2.DBTimeOfDay;

import java.sql.Connection;
import java.sql.SQLException;

public class PayInDeadlines extends CachedServer {
	private DBColumn m_cCurrencyGroup;
	
	// constructor

	public PayInDeadlines(Connection con, CurrencyGroups cgt) throws SQLException{
		super(con, "PayInDeadlines", new PayInDeadline(), "pay-in deadlines");
		this.addColumnString(true, "ID", "ID");
		m_cCurrencyGroup = new DBColumn(this, false, CurrencyGroupFK.instance(cgt), "CurrencyGroup FK", "CurrencyGroup");
		this.addColumnDefinition(m_cCurrencyGroup);
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY , "Time", "TimeOfDay"));
		this.checkStructure();
	}
	
	// overloaded. Linking pay-in schedule with its account.
	
	public void validate(DBRecord rec) throws SQLException{
		PayInDeadline pid = (PayInDeadline)rec;
		pid.getCurrencyGroup().addPayInDeadline(pid);
		if(pid.getTimeOfDay() == null) throw new SQLException(pid.getKey() + ApplicationError.isNull );
	}
		
	// accessors
	
	public PayInDeadline get(String name) throws SQLException{
		PayInDeadline pid = (PayInDeadline)find(new PayInDeadline(name));
		if (pid == null) throw new SQLException("[" + name + "]");
		return pid;
	}
	
	public DBEnumeration payInDeadlines() throws SQLException{
		return records();
	}
		
	// JAVA support
	
	public String toString(){
		return "Cached pay-in deadlines=\n" + super.toString();
	}
	
}