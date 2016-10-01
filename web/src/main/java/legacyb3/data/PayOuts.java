package legacyb3.data;
 
import java.sql.*;
import legacyb3.core.*;
import legacyb3.or2.*;

public class PayOuts extends DBTable{
	private DBColumn m_cReference;
	private DBColumn m_cAccount;
	private DBColumn m_cCurrency;
	private DBColumn m_cStatus;
									   

	public PayOuts(Connection con, Currencies ct, Accounts at) throws SQLException {
		super(con, "PayOuts", new PayOut());
		m_cReference = new DBColumn(this, true, DBString.STRING, "Reference", "Reference");
		this.addColumnDefinition(m_cReference);
		m_cAccount = new DBColumn(this, false, AccountFK.instance(at), "Account FK", "Account");
		this.addColumnDefinition(m_cAccount);
		m_cCurrency = new DBColumn(this, false, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		m_cStatus = new DBColumn(this, false, DBString.STRING, "Status", "Status");
		this.addColumnDefinition(m_cStatus);
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "Created", "TimeOfDay"));
		this.addColumnDouble(false, "Amount", "Amount");
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException {
		PayOut po = (PayOut)rec;
		if(po.getStatus() == null || po.getTimeOfDay() == null) throw new SQLException(po.getKey() + ApplicationError.isNull );
		if(po.getAmount() <= 0) throw new SQLException(po.getKey() + " amount" + ApplicationError.isNegativeOrZero );
	}
	
	// accessors
	
	public DBEnumeration payOuts() throws SQLException{
		return records();
	}
	
	public DBEnumeration payOuts(Account a, Currency c) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cAccount, "='"+ a.getID() +"'");
		dbf.setConstraint(m_cCurrency, "='"+ c.getID() +"'");
		dbf.setOrder(m_cReference, 1, true);
		return records(dbf);
	}
	
	public DBEnumeration unbookedPayOuts() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='UN'");
		return records(dbf);
	}
		
		
}
	