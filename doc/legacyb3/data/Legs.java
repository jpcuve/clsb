package legacyb3.data;

import legacyb3.core.ApplicationError;
import legacyb3.core.Leg;
import legacyb3.or2.DBColumn;
import legacyb3.or2.DBRecord;
import legacyb3.or2.DBTable;

import java.sql.Connection;
import java.sql.SQLException;

public class Legs extends DBTable{
	private DBColumn m_cTransaction;
	private DBColumn m_cCurrency;
	
	public Legs(Connection con, String tableName, Currencies ct) throws SQLException {
		super(con, tableName, new Leg(null, true, null, 0));
		m_cTransaction = new DBColumn(this, true, TransactionFK.instance(), "Tr FK", "Transaction");
		this.addColumnDefinition(m_cTransaction); // special... leave Transaction null
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		this.addColumnBoolean(false, "1 to 2", "Dir");
		this.addColumnDouble(false, "Amount", "Amount");
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		Leg l  = (Leg)rec;
		if(l.getAmount() < 0) throw new SQLException(l.getKey() + " amount" + ApplicationError.isNegativeOrZero );
	}
		
}
	
