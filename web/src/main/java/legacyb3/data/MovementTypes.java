package legacyb3.data;
 
import java.sql.*;
import legacyb3.core.*;
import legacyb3.or2.*;

public class MovementTypes extends CachedServer{
	private DBColumn m_cFromAccountType;
	private DBColumn m_cToAccountType;
	private DBColumn m_cBalanceType;

	// constructors
	
	public MovementTypes(Connection con, AccountTypes att, BalanceTypes btt) throws SQLException{
		super(con, "MovementTypes", new MovementType(null), "movement types");
		this.addColumnString(true, "ID", "ID");
		m_cFromAccountType = new DBColumn(this, false, AccountTypeFK.instance(att), "From Account Type FK", "FromAccountType");
		this.addColumnDefinition(m_cFromAccountType);
		m_cToAccountType = new DBColumn(this, false, AccountTypeFK.instance(att), "To Account Type FK", "ToAccountType");
		this.addColumnDefinition(m_cToAccountType);
		m_cBalanceType = new DBColumn(this, false, BalanceTypeFK.instance(btt), "Balance Type FK", "BalanceType");
		this.addColumnDefinition(m_cBalanceType);
		this.addColumnString(false, "Name", "Name");
		this.addColumnBoolean(false, "Save", "Save");
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		MovementType mt  = (MovementType)rec;
		if(mt.getName() == null) throw new SQLException(mt.getKey() + ApplicationError.isNull );
	}

	// accessors
	
	public MovementType get(String id) throws SQLException{
		MovementType mt = (MovementType)find(new MovementType(id));
		if (mt == null) throw new SQLException("[" + id + "]");
		return mt;
	}
	
	public DBEnumeration movementTypes() throws SQLException{
		return records();
	}
	
	public void load() throws SQLException {
		super.load();
		MovementType.in = this.get("IN");
		MovementType.ou = this.get("OU");
		MovementType.pi = this.get("PI");
		MovementType.po = this.get("PO");
		MovementType.pr = this.get("PR");
		MovementType.se = this.get("SE");
		MovementType.pa = this.get("PA");
	}				 
	
	// JAVA support
	
	public String toString(){
		return "Cached movement types=\n" + super.toString();
	}
	
}