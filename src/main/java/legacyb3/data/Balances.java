package legacyb3.data;

import legacyb3.core.Account;
import legacyb3.core.Balance;
import legacyb3.core.BalanceType;
import legacyb3.or2.DBColumn;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBFilter;
import legacyb3.or2.DBRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

public class Balances extends Server {
	private DBColumn m_cAccount;
	private DBColumn m_cBalanceType;
	private DBColumn m_cCurrency;
	
	private Accounts m_at;
		
	// constructor

	public Balances(Connection con, Accounts at, BalanceTypes btt, Currencies ct) throws SQLException{
		// super(con, "Balances", new Balance(), 5.0, "account balances"); // make sure cache is BIG
		super(con, "Balances", new Balance(), "account balances");
		m_cAccount = new DBColumn(this, true, AccountFK.instance(at), "Account FK", "Account");
		this.addColumnDefinition(m_cAccount);
		m_cBalanceType = new DBColumn(this, true, BalanceTypeFK.instance(btt), "Balance type FK", "BalanceType");
		this.addColumnDefinition(m_cBalanceType);
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		this.addColumnDouble(false, "Balance", "Amount");
		m_at = at;
		this.checkStructure();
	}
	
	// overloaded.
	
	public void validate(DBRecord rec) throws SQLException {
		Balance b = (Balance)rec;
		b.getAccount().setBalance(b);
		// b.getBalanceType().setBalance(b);
	}
		
	// accessors
	
	public DBEnumeration balances() throws SQLException{
		return records();
	}
	
	public DBEnumeration balances(BalanceType btyp) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cBalanceType, "='" + btyp.getID() + "'");
		return records(dbf);
	}
		
	// JAVA support
	
	public String toString(){
		return "Cached balances=\n" + super.toString();
	}
		
	public void save() throws SQLException{
		for(DBEnumeration e1 = m_at.accounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			/*
			for(Enumeration e2 = a.balances(); e2.hasMoreElements();){
				Balance b = (Balance)e2.nextElement();
			*/
			for(Iterator i2 = a.balances(); i2.hasNext();){
				Balance b = (Balance)i2.next();
				if(b.getTable() == null) b.setTable(this);
				save(b);
			}
		}
	}
	
}