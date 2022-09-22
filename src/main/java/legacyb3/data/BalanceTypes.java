package legacyb3.data;

import legacyb3.core.ApplicationError;
import legacyb3.core.BalanceType;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;

import java.sql.Connection;
import java.sql.SQLException;

public class BalanceTypes extends CachedServer {
	
	// constructors
	
	public BalanceTypes(Connection con) throws SQLException{
		super(con, "BalanceTypes", new BalanceType("Dummy"), "balance types");
		this.addColumnString(true, "ID", "ID");
		this.addColumnString(false, "Name", "Name");
		this.addColumnBoolean(false, "SOD", "SOD");
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		BalanceType btyp = (BalanceType)rec;
		if(btyp.getName() == null) throw new SQLException(btyp.getKey() + ApplicationError.isNull );
	}
		
	// accessors
	
	public BalanceType get(String id) throws SQLException{
		BalanceType bt = (BalanceType)find(new BalanceType(id));
		if (bt == null) throw new SQLException("[" + id + "]");
		return bt;
	}
	
	public DBEnumeration balanceTypes() throws SQLException{
		return records();
	}
	
	// miscellaneous
	
	public void load() throws SQLException {
		super.load();
		BalanceType.opn = this.get("OPN");
		BalanceType.std = this.get("STD");
		BalanceType.spl = this.get("SPL");
		BalanceType.uns = this.get("UNS");
		BalanceType.una = this.get("UNA");
		BalanceType.pin = this.get("PIN");
		BalanceType.pou = this.get("POU");
		BalanceType.sdl = this.get("SDL");
	}
			
}