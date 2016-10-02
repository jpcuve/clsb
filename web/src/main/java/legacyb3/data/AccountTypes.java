package legacyb3.data;
 
import java.sql.*;
import legacyb3.core.*;
import legacyb3.or2.*;

public class AccountTypes extends CachedServer{
	
	// constructors
	
	public AccountTypes(Connection con) throws SQLException{
		super(con, "AccountTypes", new AccountType(null), "account types");
		this.addColumnString(true, "ID", "ID");
		this.addColumnString(false, "Name", "Name");
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		AccountType actyp = (AccountType)rec;
		if(actyp.getName() == null) throw new SQLException(actyp.getKey() + ApplicationError.isNull );
	}
		
	// accessors
	
	public AccountType get(String id) throws SQLException{
		AccountType at = (AccountType)find(new AccountType(id));
		if (at == null) throw new SQLException("[" + id + "]");
		return at;
	}
	
	public DBEnumeration accountTypes() throws SQLException{
		return records();
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached account types=\n" + super.toString();
	}
	
	// miscellaneous
	
	public void load() throws SQLException {
		super.load();
		AccountType.cb = this.get("CB");
		AccountType.sm = this.get("SM");
		AccountType.lp = this.get("LP");
		AccountType.cl = this.get("CL");
		AccountType.su = this.get("SU");
	}
	
}