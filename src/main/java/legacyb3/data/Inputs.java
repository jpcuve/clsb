package legacyb3.data;

import legacyb3.core.*;
import legacyb3.or2.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

public class Inputs extends Server {
	private Deals m_dt;
	private String m_dealsTableName;
	
	private DBColumn m_cType;
	private DBColumn m_cStatus;
	private DBColumn m_cOriginator;
	private DBColumn m_cCounterparty;
	private DBColumn m_cDSM;
	private DBColumn m_cMatching;
	private DBColumn m_cBuyCurrency;
	private DBColumn m_cSellCurrency;
	
	// constructor
	
	public Inputs(Connection con, String tableName, String dealsTableName, Currencies ct, Parties pt) throws SQLException{
		super(con, tableName, new Input(null), "Inputs");
		m_dealsTableName = dealsTableName;
		this.addColumnString(true, "Reference", "Reference");
		m_cOriginator = new DBColumn(this, false, PartyFK.instance(pt, Party.class), "Originator", "Originator");
		this.addColumnDefinition(m_cOriginator);
		m_cCounterparty = new DBColumn(this, false, PartyFK.instance(pt, Party.class), "Counterparty", "CounterParty");
		this.addColumnDefinition(m_cCounterparty);
		m_cType = new DBColumn(this, false, DBString.STRING, "Type", "Type");
		this.addColumnDefinition(m_cType);
		m_cDSM = new DBColumn(this, false, PartyFK.instance(pt, SM.class), "Designated SM", "DesignatedSM");
		this.addColumnDefinition(m_cDSM);
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "Due", "TimeOfDay"));
		m_cStatus = new DBColumn(this, false, DBString.STRING, "Status", "Status");
		this.addColumnDefinition(m_cStatus);
		m_cMatching = new DBColumn(this, false, DBString.STRING, "Matching", "Match");
		this.addColumnDefinition(m_cMatching);
		this.addColumnBoolean(false, "Manual Authorise Flag", "ManualAuthoriseFlag");
		
		this.setSelector(m_cType);
		this.addPattern(InputType.GROSS, new GrossInput(null));
		m_cBuyCurrency = new DBColumn(this, false, CurrencyFK.instance(ct), "Buy currency FK", "BuyCurrency");
		this.addColumnDefinition(InputType.GROSS, m_cBuyCurrency);
		this.addColumnDouble(InputType.GROSS, false, "Buy Amount", "BuyAmount");
		m_cSellCurrency = new DBColumn(this, false, CurrencyFK.instance(ct), "Sell currency FK", "SellCurrency");
		this.addColumnDefinition(InputType.GROSS, m_cSellCurrency);
		this.addColumnDouble(InputType.GROSS, false, "Sell Amount", "SellAmount");
		m_dt = new Deals(con, dealsTableName, ct);
		this.checkStructure();
	}
	
	public void validate(DBRecord rec) throws SQLException{
		Input i  = (Input)rec;
		if(i.getTimeOfDay() == null || i.getStatus() == null) throw new SQLException(i.getKey() + ApplicationError.isNull );
	}
	// accessors
	
	public String getDealsTableName(){
		return m_dealsTableName;
	}
	
	public Input get(String name) throws SQLException{
		Input i = (Input)find(new Input(name));
		if (i == null) throw new SQLException();
		this.loadDeals(i);
		return i;
	}
	
	public DBEnumeration inputs() throws SQLException {
		return records();
	}
	
	public DBEnumeration unmatchedInputs() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.UNMATCHED + "'");
		return records(dbf);
	}
	
	public DBEnumeration unmatchedInputs(Party orig, Party cpty) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.UNMATCHED + "'");
		dbf.setConstraint(m_cOriginator, "='" + orig.getID() + "'");
		dbf.setConstraint(m_cCounterparty, "='" + cpty.getID() + "'");
		return records(dbf);
	}
	
	public DBEnumeration matchedInputs() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.MATCHED + "'");
		return records(dbf);
	}
	
	public DBEnumeration orderedMatchedInputs() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setOrder(m_cMatching, 1, true);
		dbf.setConstraint(m_cStatus, "='" + Status.MATCHED + "'");
		return records(dbf);
	}

	public DBEnumeration matchedInputs(String matchRef) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cMatching, "='" + matchRef + "'");
		return records(dbf);
	}
										  
	public DBEnumeration authorisedInputs() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.AUTHORIZED + "'");
		return records(dbf);
	}
	
	public DBEnumeration orderedAuthorizedInputs() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setOrder(m_cMatching, 1, true);
		dbf.setConstraint(m_cStatus, "='" + Status.AUTHORIZED + "'");
		return records(dbf);
	}
	
	public DBEnumeration origInputs(String orig) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cOriginator, "='" + orig + "'");
		return records(dbf);
	}
	
	public DBEnumeration inputs(DBFilter dbf) throws SQLException {
		return records(dbf);
	}
	
	public int unmatchedSize() throws SQLException {
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.UNMATCHED + "'");
		return size(dbf);
	}
	
	public int matchedSize() throws SQLException {
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.MATCHED + "'");
		return size(dbf);
	}
	
	public int authorizedSize() throws SQLException {
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.AUTHORIZED + "'");
		return size(dbf);
	}
	
	// miscellaneous
	
	public boolean unmatch() throws SQLException {
		Input i = new Input(null);
		i.setStatus(Status.UNMATCHED);
		DBPattern dbp = new DBPattern(this, i);
		dbp.addColumn(m_cStatus);
		return this.set(dbp);
	}
	
	private void loadDeals(Input inp) throws SQLException{
		if(inp.isLCF()){
			DBFilter dbf = new DBFilter(m_dt);
			dbf.setConstraint("Input FK", "='" + inp.getReference() + "'");
			for(DBEnumeration e = m_dt.records(dbf); e.hasMoreElements();){
				Deal d = (Deal)e.nextElement();
				d.setInput(inp);
				inp.setDeal(d);
			}
		}
	}
	
	// DBTable overload
	
	public DBRecord next() throws SQLException{
		DBRecord rec = super.next();
		loadDeals((Input)rec);
		return rec;
	}
	
	public boolean load(DBRecord rec) throws SQLException{
		boolean ret = super.load(rec);
		this.loadDeals((Input)rec);
		return ret;
	}
	
	public boolean save(DBRecord rec) throws SQLException{
		boolean ret = super.save(rec);
		Input inp = (Input)rec;
		if(inp.isLCF()){
			for(Iterator e = inp.deals(); e.hasNext();){
				Deal d = (Deal)e.next();
				m_dt.save(d);
			}
		}
		return ret;
	}
		
	
}