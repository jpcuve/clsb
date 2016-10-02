package legacyb3.data;

import java.sql.*;
import legacyb3.core.*;
import legacyb3.or2.*;

public class Parties extends CachedServer{
	
	private DBColumn m_cType;
	private DBColumn m_cDefaultDSM;
	
	// constructors
	
	public Parties(Connection con) throws SQLException{
		super(con, "Parties", new Party(null), "parties");
		this.addColumnString(true, "ID", "ID");
		m_cType = new DBColumn(this, false, DBString.STRING, "Type", "PartyType");
		this.addColumnDefinition(m_cType);
		this.addColumnString(false, "Name", "Name");
		this.setSelector(m_cType);
		this.addPattern(PartyType.SM, new SM(null));
		this.addPattern(PartyType.UM, new UM(null));
		m_cDefaultDSM = new DBColumn(this, false, PartyFK.instance(this, SM.class), "DefaultDSM", "DefaultDsm");
		this.addColumnDefinition(PartyType.UM, m_cDefaultDSM);
		this.addPattern(PartyType.CLSB, new CLSB(null));
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		Party p  = (Party)rec;
		if(p.getName() == null || p.getPartyType() == null) throw new SQLException(p.getKey() + ApplicationError.isNull );
	}
	
	// accessors
	
	public Party get(String id) throws SQLException{
		Party pt = (Party)find(new Party(id));
		if (pt == null) throw new SQLException("[" + id + "]");
		return pt;
	}
	
	public DBEnumeration parties() throws SQLException{
		return records();
	}
	
	public DBEnumeration parties(String patyp) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cType, "='" + patyp + "'");
		return records(dbf);
	}
	
	// JAVA support	
	public String toString(){
		return "Cached parties=\n" + super.toString();
	}

	
}