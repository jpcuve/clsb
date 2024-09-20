package legacyb3.data;

import legacyb3.core.AccountCurrency;
import legacyb3.or2.DBColumn;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountCurrencies extends CachedServer {
	private DBColumn m_cAccount;
	private DBColumn m_cCurrency;
	

	public AccountCurrencies(Connection con, Currencies ct, Accounts at) throws SQLException {
		super(con, "AccountCurrencies", new AccountCurrency(), "accounts-currencies");
		m_cAccount = new DBColumn(this, true, AccountFK.instance(at), "Account FK", "Account");
		this.addColumnDefinition(m_cAccount);
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		this.addColumnBoolean(false, "Not Eligible", "NotEligible");
		this.addColumnBoolean(false, "Suspended for pay-out", "SuspendedForPayOut");
		this.checkStructure();
	}
	
	// overloaded.
		
	public void validate(DBRecord rec) throws SQLException{
		AccountCurrency accy = (AccountCurrency)rec;
		accy.getAccount().setAccountCurrency(accy);
	}
		
	// accessors
	
	public DBEnumeration accountCurrencies() throws SQLException{
		return records();
	}
	
}
