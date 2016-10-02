package legacyb3.data;

import java.sql.*;
import java.util.Iterator;

import legacyb3.core.*;
import legacyb3.util.*;
import legacyb3.or2.*;

public class UMBalances extends Server{
	private DBColumn m_cSM;
	private DBColumn m_cUM;
	private DBColumn m_cBalanceType;
	private DBColumn m_cCurrency;
	
	private UMPositionsWithSM m_upt;
	
	// contructor 
	
	public UMBalances(Connection con, UMPositionsWithSM upt, Parties pt,BalanceTypes btt, Currencies ct) throws SQLException{
		// super(con, "UMBalances", new UMBalance(), 5.0, "UM balances"); // make sure cache is BIG
		super(con, "UMBalances", new UMBalance(), "UM balances");
		m_cSM = new DBColumn(this, true, PartyFK.instance(pt, SM.class), "SM FK", "SM");
		this.addColumnDefinition(m_cSM);
		m_cUM = new DBColumn(this, true, PartyFK.instance(pt, UM.class), "UM FK", "UM");
		this.addColumnDefinition(m_cUM);
		m_cBalanceType = new DBColumn(this, true, BalanceTypeFK.instance(btt), "Balance type FK", "BalanceType");
		this.addColumnDefinition(m_cBalanceType);
		m_cCurrency = new DBColumn(this, true, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		this.addColumnDouble(false, "Balance", "Amount");
		m_upt = upt;
		this.checkStructure();
	}	
	// methods
	
	public void validate(DBRecord rec) throws SQLException {	
        UMBalance umBal = (UMBalance)rec;
		SM sm = umBal.getSM();
		UM um = umBal.getUM();
		UMPositionWithSM anUmPos = um.getUMPos(sm);
		umBal.setUMPos(anUmPos);
		anUmPos.setBalance(umBal);
	}
	
	public void save() throws SQLException {
		for(DBEnumeration e2 = m_upt.UMPositionsWithSM(); e2.hasMoreElements();){
			UMPositionWithSM anUmPos = (UMPositionWithSM)e2.nextElement();
			for(Iterator e3 = anUmPos.umBalances(); e3.hasNext();){
				UMBalance b = (UMBalance)e3.next();
				if(b.getTable() == null) b.setTable(this);
				save(b);
			}
		}
	}

	// Enumerators
	public DBEnumeration UMBalances() throws SQLException {
		return records();
	}
	public DBEnumeration UMBalances(BalanceType btyp) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cBalanceType, "='" + btyp.getID() + "'");
		return records(dbf);
	}
	
	public DBEnumeration UMBalances(UMPositionWithSM us) throws SQLException {
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cSM, "='" + us.getSM().getID() + "'");
		dbf.setConstraint(m_cUM, "='" + us.getUM().getID() + "'");
		return records(dbf);
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
		Accounts ac =new Accounts(con,att,pt);
		BalanceTypes btt = new BalanceTypes(con);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con,cgt,ac);
		UMBalances essaiUMBal =new UMBalances(con,essaiUMPos,pt,btt,ct);
		for(DBEnumeration e = essaiUMBal.records(); e.hasMoreElements();){
			UMBalance umBal = (UMBalance)e.nextElement();
			System.out.println(umBal);
		}
	}
}
