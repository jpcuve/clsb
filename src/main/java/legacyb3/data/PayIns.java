package legacyb3.data;

import legacyb3.core.*;
import legacyb3.or2.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PayIns extends Server {
	private DBColumn m_cCurrency;
	private DBColumn m_cAccount;
	private DBColumn m_cDue;
	private DBColumn m_cStatus;
	
	private Movements m_mt;
										   
	public PayIns(Connection con, Currencies ct, Accounts at, Movements mt) throws SQLException {
		super(con, "PayIns", new PayIn(), "Pay-ins");
		m_cAccount = new DBColumn(this, true, AccountFK.instance(at), "Account FK", "Account");
		this.addColumnDefinition(m_cAccount);
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		m_cDue = new DBColumn(this, true, DBTimeOfDay.TIME_OF_DAY, "Due", "TimeOfDay");
		this.addColumnDefinition(m_cDue);
		m_cStatus = new DBColumn(this, false, DBString.STRING, "Status", "Status");
		this.addColumnDefinition(m_cStatus);
		this.addColumnDouble(false, "Amount", "Amount");
		m_mt = mt;
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException {
		PayIn pi = (PayIn)rec;
		if(pi.getStatus() == null) throw new SQLException(pi.getKey() + ApplicationError.isNull );
		if(pi.getAmount() <= 0) throw new SQLException(pi.getKey() + " amount" + ApplicationError.isNegativeOrZero );
	}
	
	// accessors
	
	public DBEnumeration payIns() throws SQLException{
		return records();
	}
	
	public DBEnumeration payIns(Account a, Currency c) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cAccount, "='"+ a.getID() +"'");
		dbf.setConstraint(m_cCurrency, "='"+ c.getID() +"'");
		dbf.setOrder(m_cDue, 1, true);
		return records(dbf);
	}
	
	public DBEnumeration readyPayIns() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.READY + "'");
		return records(dbf);
	}
	
	public DBEnumeration bookedPayIns() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.BOOKED + "'");
		return records(dbf);
	}
	
	public DBEnumeration readyPayInsBefore(TimeOfDay t) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cDue, "<=#" + t.hour  + ":" + t.min + ":00#");
		dbf.setConstraint(m_cStatus, "='" + Status.READY + "'");
		return records(dbf);
	}
	
	public int readyPayInsBeforeSize(TimeOfDay t) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cDue, "<=#" + t.hour  + ":" + t.min + ":00#");
		dbf.setConstraint(m_cStatus, "='" + Status.READY + "'");
		return size(dbf);
	}
	
	// miscellaneous
	
	public void ready() throws SQLException{
		PayIn pi = new PayIn();
		pi.setStatus(Status.READY);
		DBPattern dbp = new DBPattern(this, pi);
		dbp.addColumn(m_cStatus);
		this.set(dbp);
	}
	
	public int book(TimeOfDay tod) throws SQLException{
		this.progress("Booking pay-ins", 0);
		int psize = this.readyPayInsBeforeSize(tod);
		int pcount = 0;
		for(DBEnumeration e1 = this.readyPayInsBefore(tod); e1.hasMoreElements();){
			PayIn p = (PayIn)e1.nextElement();
			pcount++;
			this.progress("Processing pay-in " + pcount + " of " + psize, (pcount * 100) / psize);
			m_mt.book(p, tod);
			p.setStatus(Status.BOOKED);
			p.save();
		}
		this.progress("Booked " + pcount + " pay-ins", 0);
		this.setChanged();
		return pcount;
	}
}
