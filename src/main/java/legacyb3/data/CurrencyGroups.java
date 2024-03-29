package legacyb3.data;

import legacyb3.core.ApplicationError;
import legacyb3.core.CurrencyGroup;
import legacyb3.core.CurrencyGroupIterator;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

public class CurrencyGroups extends CachedServer {
	
	// constructors
	
	public CurrencyGroups(Connection con) throws SQLException{
		super(con, "CurrencyGroups", new CurrencyGroup(null), "currency groups");
		this.addColumnString(true, "ID", "ID");
		this.addColumnLong(false, "Priority", "Priority");
		this.addColumnString(false, "Name", "Name");
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		CurrencyGroup cg  = (CurrencyGroup)rec;
		if(cg.getName() == null) throw new SQLException(cg.getKey() + ApplicationError.isNull );
	}
		
	// accessors
	
	public CurrencyGroup get(String name) throws SQLException{
		CurrencyGroup cg = (CurrencyGroup)find(new CurrencyGroup(name));
		if (cg == null) throw new SQLException("[" + name + "]");
		return cg;
	}
	
	public DBEnumeration currencyGroups() throws SQLException{
		return records();
	}
	
	public Iterator currencyGroupsPerPriority(boolean dir) throws SQLException{
		return new CurrencyGroupIterator(this, dir);
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached currency groups=\n" + super.toString();
	}	
}