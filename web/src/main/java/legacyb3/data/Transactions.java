package legacyb3.data;
 
import java.sql.*;
import java.util.Iterator;
import java.util.Vector;

import legacyb3.core.*;
import legacyb3.util.*;
import legacyb3.or2.*;

public class Transactions extends Server {
	private Legs m_lt;
	private Movements m_mt;
	private String m_legsTableName;
	
	private DBColumn m_cReference;
	private DBColumn m_cAccount1;
	private DBColumn m_cAccount2;
	private DBColumn m_cType;
	private DBColumn m_cStatus;
	private DBColumn m_cBuyCurrency;
	private DBColumn m_cSellCurrency;
	
	// constructor
	
	public Transactions(Connection con, String tableName, String legsTableName, Currencies ct, Accounts at, Movements mt) throws SQLException{
		super(con, tableName, new Transaction(null), tableName);
		m_cReference = new DBColumn(this, true, DBString.STRING, "Reference", "Reference");
		this.addColumnDefinition(m_cReference);
		this.addColumnString(false, "Parent reference", "ParentReference");
		m_cAccount1 = new DBColumn(this, false, AccountFK.instance(at), "Account 1 FK", "FirstAccount");
		this.addColumnDefinition(m_cAccount1);
		m_cAccount2 = new DBColumn(this, false, AccountFK.instance(at), "Account 2 FK", "SecondAccount");
		this.addColumnDefinition(m_cAccount2);
		m_cType = new DBColumn(this, false, DBString.STRING, "Type", "Type");
		this.addColumnDefinition(m_cType);
		m_cStatus = new DBColumn(this, false, DBString.STRING, "Status", "Status");
		this.addColumnDefinition(m_cStatus);
		this.setSelector(m_cType);
		this.addPattern(TransactionType.GROSS, new GrossTransaction(null));
		m_cBuyCurrency = new DBColumn(this, false, CurrencyFK.instance(ct), "Buy currency FK", "BuyCurrency");
		this.addColumnDefinition(TransactionType.GROSS, m_cBuyCurrency);
		this.addColumnDouble(TransactionType.GROSS, false, "Buy amount", "BuyAmount");
		m_cSellCurrency = new DBColumn(this, false, CurrencyFK.instance(ct), "Sell currency FK", "SellCurrency");
		this.addColumnDefinition(TransactionType.GROSS, m_cSellCurrency);
		this.addColumnDouble(TransactionType.GROSS, false, "Sell amount", "SellAmount");
		m_mt = mt;
		m_legsTableName = legsTableName;
		this.checkStructure();
		m_lt = new Legs(con, legsTableName, ct);
	}
	
	public void validate(DBRecord rec) throws SQLException {
		Transaction t = (Transaction)rec;
		if(t.getStatus() == null) throw new SQLException(t.getKey() + ApplicationError.isNull );
	}
	
	// accessors
	
	public String getLegsTableName(){
		return m_legsTableName;
	}
	
	public Movements getMovements(){
		return m_mt;
	}

	public Transaction get(String name) throws SQLException{
		Transaction t = (Transaction)find(new Transaction(name));
		if (t == null) throw new SQLException();
		this.loadLegs(t);
		return t;
	}
	
	public DBEnumeration transactions() throws SQLException {
		return records();
	}
	
	public DBEnumeration transactions(DBFilter dbf) throws SQLException {
		return records(dbf);
	}
	
	public DBEnumeration settlementMatureTransactions() throws SQLException {
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.SETTLEMENTMATURE + "'");
		dbf.setOrder(m_cReference, 1, true);
		return records(dbf);
	}
	
	public int settlementMatureSize() throws SQLException {
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cStatus, "='" + Status.SETTLEMENTMATURE + "'");
		return size(dbf);
	}
	
	public DBRecord next() throws SQLException{
		DBRecord rec = super.next();
		this.loadLegs((Transaction)rec);
		return rec;
	}
	
	// miscellaneous
	
	public void build(AuthorizationQueue iq) throws SQLException {
		this.progress("Assembling transaction queue", 0);
		int pcount = 0;
		int transCount = 0;
		int psize = iq.authorizedSize();
		Vector inp = new Vector();
		String match = null;
		DBEnumeration e1 = iq.orderedAuthorizedInputs();
		while(e1.hasMoreElements()){
			Input ip = (Input)e1.nextElement();
			pcount++;
			this.progress("Processing input " + pcount + " of " + psize, (pcount * 100) / psize);
			if(inp.size() == 0){
				match = ip.getMatch();
				inp.addElement(ip);
			}else if(ip.getMatch().equals(match)){
				inp.addElement(ip);
			}
			if(!ip.getMatch().equals(match) || !e1.hasMoreElements()){
				Input i1 = (Input)inp.elementAt(0);
				Input i2 = (Input)inp.elementAt(inp.size() - 1);
				Transaction t = null;
				if(!i1.isLCF() && !i2.isLCF()){
					GrossTransaction gt = new GrossTransaction(match);
					GrossInput gi1 = (GrossInput)i1;
					GrossInput gi2 = (GrossInput)i2;
					gt.setType(TransactionType.GROSS);
					gt.setSellCurrency(gi1.getSellCurrency());
					gt.setSellAmount(gi1.getSellAmount());
					gt.setBuyCurrency(gi2.getSellCurrency());
					gt.setBuyAmount(gi2.getSellAmount());
					t = gt;
				}else{
					t = new Transaction(match);
					t.setType(TransactionType.LCF);
					for(Iterator e2 = i1.deals(); e2.hasNext();){
						Deal d = (Deal)e2.next();
						Currency ccy = d.getCurrency();
						boolean dir = true;
						double am = d.getAmount();
						if(!d.isSell()){
							dir = false;
							am = i2.getDeal(d.getCurrency()).getAmount();
						}
						t.setLeg(new Leg(t, dir, ccy, am));
					}
				}
				t.setFirstAccount(i1.getDesignatedSM().getAccount());
				t.setSecondAccount(i2.getDesignatedSM().getAccount());
				StringBuffer sb = new StringBuffer(1024);
				for(int i = 0; i < inp.size(); i++) sb.append(((sb.length() == 0) ? "" : "+") + ((Input)inp.elementAt(i)).getReference());
				t.setParentReference(sb.toString());
				t.setStatus(Status.SETTLEMENTMATURE );
				System.out.println(t);
				for(int i = 0; i < inp.size(); i++){
					Input in = (Input)inp.elementAt(i);
					in.setStatus(Status.EXPIRED );
					in.save();
				}
				this.save(t);
				transCount++;
				this.getMovements().paBook(t, true);
				inp = new Vector();
				match = ip.getMatch();
				inp.addElement(ip);
			}
		}
		this.progress("Assembled " + transCount + " transactions", 0);
	}
	
	/*
	public void build(AuthorizationQueue iq) throws SQLException {
		if(al != null) al.appProgress(new AppEvent(this, "Building matched references btree", 0));
		int pcount = 0;
		int psize = iq.authorizedSize();
		BTree refs = new BTree(new StringComparator());
		for(DBEnumeration e1 = iq.authorisedInputs(); e1.hasMoreElements();){
			Input inp = (Input)e1.nextElement();
			pcount++;
			if(al != null) al.appProgress(new AppEvent(this, "Processing input " + pcount + " of " + psize, (pcount * 100) / psize / 4));
			refs.addElement(inp.getMatch());
		}
		if(al != null) al.appProgress(new AppEvent(this, "Processed " + psize + " inputs", 0));
		System.out.println(refs);
		if(al != null) al.appProgress(new AppEvent(this, "Assembling transaction queue", 0));
		pcount = 0;
		psize = refs.size();
		for(Enumeration e1 = refs.elements(); e1.hasMoreElements();){
			String match = (String)e1.nextElement();
			pcount++;
			if(al != null) al.appProgress(new AppEvent(this, "Processing transaction " + pcount + " of " + psize, 25 + (pcount * 100) / psize * 3 / 4));
			Vector inp = new Vector();
			for(DBEnumeration e2 = iq.authorisedInputs(match); e2.hasMoreElements();) inp.addElement((Input)e2.nextElement());
			int size = inp.size();
			if(size > 0){
				Input i1 = (Input)inp.elementAt(0);
				Input i2 = (Input)inp.elementAt(inp.size() - 1);
				Transaction t = null;
				if(!i1.isLCF() && !i2.isLCF()){
					GrossTransaction gt = new GrossTransaction(match);
					GrossInput gi1 = (GrossInput)i1;
					GrossInput gi2 = (GrossInput)i2;
					gt.setType(TransactionType.GROSS);
					gt.setSellCurrency(gi1.getSellCurrency());
					gt.setSellAmount(gi1.getSellAmount());
					gt.setCurrency(gi2.getSellCurrency());
					gt.setAmount(gi2.getSellAmount());
					t = gt;
				}else{
					t = new Transaction(match);
					t.setType(TransactionType.LCF);
					for(Enumeration e2 = i1.deals(); e2.hasMoreElements();){
						Deal d = (Deal)e2.nextElement();
						Currency ccy = d.getCurrency();
						boolean dir = true;
						double am = d.getAmount();
						if(!d.isSell()){
							dir = false;
							am = i2.getDeal(d.getCurrency()).getAmount();
						}
						t.setLeg(new Leg(t, dir, ccy, am));
					}
				}
				t.setFirstAccount(i1.getDesignatedSM().getAccount());
				t.setSecondAccount(i2.getDesignatedSM().getAccount());
				StringBuffer sb = new StringBuffer(1024);
				for(int i = 0; i < size; i++) sb.append(((sb.length() == 0) ? "" : "+") + ((Input)inp.elementAt(i)).getReference());
				t.setParentReference(sb.toString());
				t.setStatus(Status.SETTLEMENTMATURE );
				System.out.println(t);
				for(int i = 0; i < size; i++){
					Input in = (Input)inp.elementAt(i);
					in.setStatus(Status.EXPIRED );
					in.save();
				}
				this.save(t);
				this.getMovements().paBook(t, true);
			}
		}
		if(al != null) al.appProgress(new AppEvent(this, "Assembled " + psize + " transactions", 0));
	}
	*/
	
	private void loadLegs(Transaction tr) throws SQLException{
		if(tr.isLCF()){	
			DBFilter dbf = new DBFilter(m_lt);
			dbf.setConstraint("Tr FK", "='" + tr.getReference() + "'");
			for(DBEnumeration e = m_lt.records(dbf); e.hasMoreElements();){
				Leg l = (Leg)e.nextElement();
				l.setTransaction(tr);
				tr.setLeg(l);
			}
		}
	}
	
	public boolean load(DBRecord rec) throws SQLException{
		boolean ret = super.load(rec);
		this.loadLegs((Transaction)rec);
		return ret;
	}
	

	public boolean save(DBRecord rec) throws SQLException{
		boolean ret = super.save(rec);
		Transaction tr = (Transaction)rec;
		if(tr.isLCF()){
			for(Iterator e = tr.legs(); e.hasNext();){
				Leg l = (Leg)e.next();
				m_lt.save(l);
			}
		}
		return ret;
	}
	
	/*
	public boolean query(DBRecord rec) throws SQLException{
		boolean ret = super.query(rec);
		loadLegs((Transaction)rec);
		return ret;
	}
	

	public void insert(DBRecord rec) throws SQLException, IllegalAccountException{
		super.insert(rec);
		for(Enumeration e = ((Transaction)rec).legs(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			lt.insert(l);
		}
	}
	*/

}