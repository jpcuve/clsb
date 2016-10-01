package legacyb3.data;
 
import java.sql.*;
import java.util.Enumeration;

import legacyb3.core.*;
import legacyb3.util.*;
import legacyb3.or2.*;

public class Movements extends Server {
	private DBColumn m_cMovementType;
	private DBColumn m_cDBAccount;
	private DBColumn m_cCRAccount;
	private DBColumn m_cCurrency;
	
	public Movements(Connection con, MovementTypes mtt, Currencies ct, Accounts at) throws SQLException{
		super(con, "Movements", new Movement(), "Movements");
		this.addColumnString(true, "Reference", "Reference");
		m_cMovementType = new DBColumn(this, false, MovementTypeFK.instance(mtt), "Movement type FK", "MovementType");
		this.addColumnDefinition(m_cMovementType);
		m_cDBAccount = new DBColumn(this, false, AccountFK.instance(at), "Account DB FK", "DBAccount");
		this.addColumnDefinition(m_cDBAccount);
		m_cCRAccount = new DBColumn(this, false, AccountFK.instance(at), "Account CR FK", "CRAccount");
		this.addColumnDefinition(m_cCRAccount);
		m_cCurrency = new DBColumn(this, false, CurrencyFK.instance(ct), "Currency FK", "Currency");
		this.addColumnDefinition(m_cCurrency);
		this.addColumnDouble(false, "Amount", "Amount");
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "Book", "Book"));
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		Movement m  = (Movement)rec;
		if(m.getParentReference() == null) throw new SQLException(m.getKey() + ApplicationError.isNull );
		if(m.getAmount() <= 0) throw new SQLException(m.getKey() + " amount" + ApplicationError.isNegativeOrZero );
	}
	
	public DBEnumeration movements() throws SQLException{
		return records();
	}
	
	public DBEnumeration movements(Currency c) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cCurrency, "='" + c.getID() + "'");
		return records(dbf);
	}
	
	// miscellaneous
	
	public void book(Bookable b, TimeOfDay ts) throws SQLException{
		for(Enumeration e1 = b.movements(); e1.hasMoreElements();){
			Movement m = (Movement)e1.nextElement();
			m.setTable(this);
			m.book(ts);
		}
	}
	
	public void prBook(Settlable s, boolean dir) throws SQLException{
		for(Enumeration e1 = s.prMovements(dir); e1.hasMoreElements();){
			Movement m = (Movement)e1.nextElement();
			m.setTable(this);
			m.book();
		}
	}
	
	public void paBook(Transaction t, boolean dir) throws SQLException{
		for(Enumeration e1 = t.movements(MovementType.pa, dir); e1.hasMoreElements();){
			Movement m = (Movement)e1.nextElement();
			m.setTable(this);
			m.book();
		}
	}
	
}
