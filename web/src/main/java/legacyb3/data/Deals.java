package legacyb3.data;
 
import java.sql.*;
import legacyb3.core.*;
import legacyb3.or2.*;

public class Deals extends DBTable {
	private DBColumn m_cInput;
	private DBColumn m_cCurrency;
	
	public Deals(Connection con, String tableName, Currencies ct) throws SQLException {
		super(con, tableName, new Deal(null));
		m_cInput = new DBColumn(this, true, InputFK.instance(), "Input FK", "Input"); // special... leave Input null
		this.addColumnDefinition(m_cInput);
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		this.addColumnBoolean(false, "Sell", "Sell");
		this.addColumnDouble(false, "Amount", "Amount");
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		Deal d  = (Deal)rec;
		if(d.getAmount() <= 0) throw new SQLException(d.getKey() + " amount" + ApplicationError.isNegativeOrZero );
	}
	
}
	
