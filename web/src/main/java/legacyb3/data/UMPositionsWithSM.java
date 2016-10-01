package legacyb3.data;

import java.sql.*;
import legacyb3.core.*;
import legacyb3.or2.*;

public class UMPositionsWithSM extends CachedServer {
	private DBColumn m_cSM;
	private DBColumn m_cUM;
	
	// constructors
	
	public UMPositionsWithSM(Connection con, Parties pt) throws SQLException{
		super(con, "UMPositionsWithSM", new UMPositionWithSM(null,null), "UMPositionsWithSM");
		m_cSM = new DBColumn(this, true, PartyFK.instance(pt, SM.class), "SM FK", "SM");
		this.addColumnDefinition(m_cSM);
		m_cUM = new DBColumn(this, true, PartyFK.instance(pt, UM.class), "UM FK", "UM");
		this.addColumnDefinition(m_cUM);
		this.addColumnDouble(false, "ASPL", "ASPL");
		this.addColumnDouble(false, "CRL", "CRL");
		this.addColumnDouble(false, "ASPL SDT", "APSLSDT");
		this.addColumnDouble(false, "CRL SDT", "CRLSDT");
		this.checkStructure();
	}
	
	// overloaded. 
	
	public void validate(DBRecord rec) throws SQLException {
		UMPositionWithSM umPos = (UMPositionWithSM)rec;
		SM sm = umPos.getSM();
		UM um = umPos.getUM();
		// UM um = (UM)pt.get(umPos.getUM().getID());
	    um.setUMPos(umPos);//added
	}
		
	// accessors
	
	public UMPositionWithSM get(SM sm,UM um) throws SQLException{
		UMPositionWithSM umPos = (UMPositionWithSM)find(new UMPositionWithSM(sm,um));
		//if (umPos == null) throw new IllegalUMPositionException();
		return umPos;
	}
	
	public DBEnumeration UMPositionsWithSM() throws SQLException {
		return records();
	}
	
	public DBEnumeration  UMPositionsWithSM(SM sm) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cSM, "='" + sm.getID() +"'");
		return records(dbf);
	}
	public DBEnumeration  UMPositionsWithSM(UM um) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cUM, "='" + um.getID() +"'");
		return records(dbf);
	}
	
	
	// JAVA support
	
	public String toString(){
		return "Cached UM Positions with SM=\n" + super.toString();
	}
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Class.forName ("com.ms.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:test");
		AccountTypes att = new AccountTypes(con);
		Parties pt = new Parties(con);
		UMPositionsWithSM essaiUMPos = new UMPositionsWithSM(con,pt);
		for(DBEnumeration e = essaiUMPos.records(); e.hasMoreElements();){
			UMPositionWithSM umPos = (UMPositionWithSM)e.nextElement();
			System.out.println(umPos);
		}
	}
}

