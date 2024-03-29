package legacyb3.data;

import legacyb3.core.ApplicationError;
import legacyb3.core.RTGSCutOff;
import legacyb3.or2.DBColumn;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;
import legacyb3.or2.DBTimeOfDay;

import java.sql.Connection;
import java.sql.SQLException;

public class RTGSCutOffs extends CachedServer {
	private DBColumn m_cCurrency;
	
	// constructors
	
	public RTGSCutOffs(Connection con, Currencies ct) throws SQLException {
		super(con, "RTGSCutOffs", new RTGSCutOff(), "RTGS cut-offs");
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		this.addColumnDefinition(new DBColumn(this, true, DBTimeOfDay.TIME_OF_DAY, "Start", "Start"));
		this.addColumnDefinition(new DBColumn(this, true, DBTimeOfDay.TIME_OF_DAY, "End", "End"));
		this.checkStructure();
	}
	
	// overloaded. 
	
	public void validate(DBRecord rec) throws SQLException {
		RTGSCutOff co = (RTGSCutOff)rec;
		if(co.getStart() == null || co.getEnd() == null) throw new SQLException(co.getKey() + ApplicationError.isNull );
		if(co.getEnd().isBefore(co.getStart())) throw new SQLException(co.getKey() + " start" + ApplicationError.isAfter + " end");
		co.getCurrency().addRTGSCutOff(co);
	}
		
	// accessors
	
	public DBEnumeration rtgsCutOffs() throws SQLException{
		return records();
	}
}
