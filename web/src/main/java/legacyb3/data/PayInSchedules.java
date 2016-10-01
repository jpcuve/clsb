package legacyb3.data;
 
import java.util.Date;
import java.sql.*;
import java.util.Iterator;

import legacyb3.core.*;
import legacyb3.or2.*;
import legacyb3.util.*;

public class PayInSchedules extends Server {
	private DBColumn m_cCurrency;
	private DBColumn m_cAccount;
	private DBColumn m_cPayInDeadline;
	private DBColumn m_cPayInScheduleType;
	
	private Accounts m_at;
	
	// constructor

	public PayInSchedules(Connection con, Accounts at, Currencies ct, PayInDeadlines pidt) throws SQLException{
		super(con, "PayInSchedules", new PayInSchedule(), "pay-in schedules");
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		m_cAccount = new DBColumn(this, true, AccountFK.instance(at), "Account FK", "Account");
		this.addColumnDefinition(m_cAccount);
		m_cPayInDeadline = new DBColumn(this, true, PayInDeadlineFK.instance(pidt), "PayInDeadline FK", "PayInDeadline");
		this.addColumnDefinition(m_cPayInDeadline);
		m_cPayInScheduleType = new DBColumn(this, true, DBString.STRING, "PayInScheduleType FK", "PayInScheduleType");
		this.addColumnDefinition(m_cPayInScheduleType);
		this.addColumnDouble(false, "Amount", "Amount");
		m_at = at;
		this.checkStructure();
	}
	
	// overloaded.
	
	public void validate(DBRecord rec) throws SQLException {
		PayInSchedule pis = (PayInSchedule)rec;
		if(pis.getAmount() < 0) throw new SQLException(pis.getKey() + " amount" + ApplicationError.isNegative);
		pis.getAccount().setPayInScheduleFlow(pis);
	}
		
	// accessors
	
	public DBEnumeration payInSchedules() throws SQLException{
		return records();
	}
	
	public DBEnumeration payInSchedules(String pistyp) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cPayInScheduleType, "='" + pistyp + "'");
		return records(dbf);
	}
		
	// JAVA support
	
	public String toString(){
		return "Cached pay-in schedules=\n" + super.toString();
	}
	
	public void save() throws SQLException{
		for(DBEnumeration e1 = m_at.settlementMemberAccounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			/*
			for(Enumeration e2 = a.payInSchedules(); e2.hasMoreElements();){
				PayInSchedule pi = (PayInSchedule)e2.nextElement();
			*/
			for(Iterator i2 = a.payInSchedules(); i2.hasNext();){
				PayInSchedule pi = (PayInSchedule)i2.next();
				if(pi.getTable() == null){
					pi.setTable(this);
					if(pi.getAmount() > pi.getCurrency().zero()) save(pi);
				}else{
					save(pi);
				}
			}
		}
	}
	
}