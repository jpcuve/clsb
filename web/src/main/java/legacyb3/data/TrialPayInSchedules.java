package legacyb3.data;
 
import java.util.Date;
import java.sql.*;
import java.util.Iterator;
import java.util.TreeMap;

import legacyb3.core.*;
import legacyb3.or2.*;
import legacyb3.util.*;

public class TrialPayInSchedules extends DBTable{
	private DBColumn m_cCurrency;
	private DBColumn m_cAccount;
	private DBColumn m_cPayInDeadline;
	
	// constructor

	public TrialPayInSchedules(Connection con, Accounts at, Currencies ct, PayInDeadlines pidt) throws SQLException{
		super(con, "TrialPayInSchedules", new TrialPayInSchedule());
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		m_cAccount = new DBColumn(this, true, AccountFK.instance(at), "Account FK", "Account");
		this.addColumnDefinition(m_cAccount);
		m_cPayInDeadline = new DBColumn(this, true, PayInDeadlineFK.instance(pidt), "PayInDeadline FK", "PayInDeadline");
		this.addColumnDefinition(m_cPayInDeadline);
		this.addColumnDouble(false, "Amount", "Amount");
		this.checkStructure();
	}
	
	// overloaded.
	
	public void validate(DBRecord rec) throws SQLException {
		TrialPayInSchedule pis = (TrialPayInSchedule)rec;
		if(pis.getAmount() < 0) throw new SQLException(pis.getKey() + " amount" + ApplicationError.isNegative);
	}
		
	// accessors
	
	public DBEnumeration trialPayInSchedules() throws SQLException{
		return records();
	}
	
	// miscellaneous
	
	public void loadIntoCurrentPayInSchedules() throws SQLException {
		/*
		BinaryTree bt = new BinaryTree(new AccountComparator());
		for(DBEnumeration e1 = this.trialPayInSchedules(); e1.hasMoreElements();){
			TrialPayInSchedule tpis = (TrialPayInSchedule)e1.nextElement();
			Account a = tpis.getAccount();
			bt.addElement(a);
		}
		for(Enumeration e1 = bt.elements(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			a.movePayInSchedules(PayInScheduleType.CURRENT , PayInScheduleType.PREVIOUS );
		}
		*/
		TreeMap tm = new TreeMap();
		for(DBEnumeration e1 = this.trialPayInSchedules(); e1.hasMoreElements();){
			TrialPayInSchedule tpis = (TrialPayInSchedule)e1.nextElement();
			Account a = tpis.getAccount();
			tm.put(a.getID(), a);
		}
		for(Iterator i1 = tm.values().iterator(); i1.hasNext();){
			Account a = (Account)i1.next();
			a.movePayInSchedules(PayInScheduleType.CURRENT , PayInScheduleType.PREVIOUS );
		}
		for(DBEnumeration e1 = this.trialPayInSchedules(); e1.hasMoreElements();){
			TrialPayInSchedule tpis = (TrialPayInSchedule)e1.nextElement();
			Account a = tpis.getAccount();
			Currency ccy = tpis.getCurrency();
			PayInDeadline pid = tpis.getPayInDeadline();
			PayInSchedule pis = a.getPayInScheduleFlow(PayInScheduleType.CURRENT , ccy, pid);
			if(pis == null){
				a.setPayInScheduleFlow(new PayInSchedule(a, tpis.getCurrency(), tpis.getPayInDeadline(), PayInScheduleType.CURRENT , tpis.getAmount()));
			}else{
				pis.setAmount(tpis.getAmount());
			}
		}
	}
	
}