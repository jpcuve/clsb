package legacyb3.or2;

import java.util.*;
import java.sql.*;

public abstract class DBTable extends Observable implements Observer{
	
	private static final String BASE = "";
	
	private String m_name;
	private Connection m_con;
	private String m_sql;
	private HashMap m_cds;
	private HashMap m_patterns;
	private HashMap m_allcds;
	private DBColumn m_selector;
	
	public DBTable(Connection c, String s, DBRecord p){
		m_con = c;
		m_name = s;
		m_cds = new HashMap();
		m_patterns = new HashMap();
		m_patterns.put(BASE, p);
		m_allcds = new HashMap();
	}
	
	public void addColumnString(boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(new DBColumn(this, pKey, DBString.STRING, fName, pName));
	}
	
	public void addColumnLong(boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(new DBColumn(this, pKey, DBLong.LONG, fName, pName));
	}
	
	public void addColumnInteger(boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(new DBColumn(this, pKey, DBInteger.INTEGER, fName, pName));
	}
	
	public void addColumnDouble(boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(new DBColumn(this, pKey, DBDouble.DOUBLE, fName, pName));
	}
	
	public void addColumnBoolean(boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(new DBColumn(this, pKey, DBBoolean.BOOLEAN , fName, pName));
	}
	
	public void addColumnDate(boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(new DBColumn(this, pKey, DBDate.DATE , fName, pName));
	}
	
	public void addColumnString(Object cl, boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(cl, new DBColumn(this, pKey, DBString.STRING, fName, pName));
	}
	
	public void addColumnLong(Object cl, boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(cl, new DBColumn(this, pKey, DBLong.LONG, fName, pName));
	}
	
	public void addColumnInteger(Object cl, boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(cl, new DBColumn(this, pKey, DBInteger.INTEGER, fName, pName));
	}
	
	public void addColumnDouble(Object cl, boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(cl, new DBColumn(this, pKey, DBDouble.DOUBLE, fName, pName));
	}
	
	public void addColumnBoolean(Object cl, boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(cl, new DBColumn(this, pKey, DBBoolean.BOOLEAN , fName, pName));
	}
	
	public void addColumnDate(Object cl, boolean pKey, String fName, String pName) throws SQLException {
		this.addColumnDefinition(cl, new DBColumn(this, pKey, DBDate.DATE , fName, pName));
	}
	
	public void addColumnDefinition(DBColumn c) throws SQLException {
		this.addColumnDefinition(BASE, c);
	}
	
	public void addColumnDefinition(Object cl, DBColumn c) throws SQLException {
		if(cl != BASE) this.checkSelector();
		m_allcds.put(c.getFieldName(), c);
		Vector v = (Vector)m_cds.get(cl);
		if(v == null){
			v = new Vector();
			m_cds.put(cl, v);
		}
		v.addElement(c);
	}
	
	public void addPattern(Object cl, DBRecord r, String ex) throws SQLException {
		if(cl != BASE) this.checkSelector();
		r.setTable(this);
		m_patterns.put(cl, r);
		for(Iterator i = this.columnDefinitions(ex); i.hasNext();) this.addColumnDefinition(cl, (DBColumn)i.next());
	}
	
	private void checkSelector() throws SQLException {
		if(m_selector == null) throw new SQLException("Table " + m_name + ": undefined selector column.");
	}
	
	public void addPattern(Object cl, DBRecord r) throws SQLException {
		this.addPattern(cl, r, null);
	}
	
	public DBRecord getPattern(Object cl) throws SQLException {
		DBRecord r = (DBRecord)m_patterns.get(cl);
		if(r == null) r = (DBRecord)m_patterns.get(BASE);
		return r;
	}
	
	public DBRecord getPattern() throws SQLException {
		return this.getPattern(BASE);
	}
	
	public void setSelector(DBColumn c){
		m_selector = c;
	}
		
	public DBColumn getSelector(){
		return m_selector;
	}
	
	public Iterator columnDefinitions(Object cl) throws SQLException {
		Vector v = (Vector)m_cds.get(cl);
		if(v == null) v = (Vector)m_cds.get(BASE);
		return v.iterator();
	}
	
	public Iterator columnDefinitions() throws SQLException {
		return m_allcds.values().iterator();
	}
	
	public Iterator columnDefinitions(DBRecord r) throws SQLException {
		Object cl = null;
		if(m_selector != null){
			cl = m_selector.getProperty(r);
		}
		return this.columnDefinitions(cl);
	}

	public DBColumn getColumnDefinition(String name){
		return (DBColumn)m_allcds.get(name);
	}
	
	public boolean isColumnDefinition(DBColumn c){
		return (m_allcds.get(c.getFieldName()) != null);
	}
	
	// accessors
	
	public String getName(){
		return m_name;
	}
	
	public String getCompatibleName(){
		return (m_name.indexOf(" ") == -1) ? m_name : ("[" + m_name + "]");
	}
	
	public Connection getConnection(){
		return m_con;
	}

	private void setSQL(String sql){
		m_sql = sql;
	}
	
	private String getSQL(){
		// System.out.println(m_sql);
		return m_sql;
	}
	
	public DBEnumeration records() throws SQLException{
		return new DBRecordIterator(this);
	}
	
	public DBEnumeration records(DBFilter dbf) throws SQLException{
		return new DBRecordIterator(dbf);
	}
	
	// SQL core
	
	public DBFilter getKeyFilter(DBRecord r) throws SQLException{
		DBFilter dbf = new DBFilter(this);
		for(Iterator i = this.columnDefinitions(); i.hasNext();){
			DBColumn c = (DBColumn)i.next();
			if(c.isPrimaryKey()){
				String s = c.getSQL(r);
				if (s != null){
					dbf.setConstraint(c, "=" + s);
				}else{
					throw new SQLException("DBRecord undefined key.");
				}
			}
		}
		return dbf;
	}
	
	
	// SELECT support
	
	private String getSELECT(){
		return "SELECT * FROM " + this.getCompatibleName();
	}
	
	private String getSELECT(DBRecord r) throws SQLException{
		StringBuffer b = new StringBuffer(1024);
		for(Iterator i = this.columnDefinitions(); i.hasNext();){
			DBColumn c = (DBColumn)i.next();
			b.append((b.length() == 0) ? "SELECT " : ", ");
			b.append(c.getCompatibleName());
		}
		if (b.length() != 0){
			b.append(" FROM " + this.getCompatibleName() + this.getKeyFilter(r).getWhereClause());
		}
		return b.toString();
	}

	// UPDATE support
	
	private String getUPDATE(DBPattern dbp, DBFilter dbf) throws SQLException{
		StringBuffer b = new StringBuffer(1024);
		b.append("UPDATE " + this.getCompatibleName());
		b.append(dbp.getSet());
		if(dbf != null) b.append(dbf.getWhereClause());
		return b.toString();
	}
	
	private String getUPDATE(DBRecord r) throws SQLException{
		StringBuffer b = new StringBuffer(1024);
		for(Iterator i = this.columnDefinitions(r); i.hasNext();){
			DBColumn c = (DBColumn)i.next();
			if (!c.isPrimaryKey()){
				b.append((b.length() == 0) ? " SET " : ", ");
				b.append(c.getCompatibleName() + " = " + c.getSQL(r));
			}
		}
		if (b.length() != 0){
			b.insert(0, "UPDATE " + this.getCompatibleName());
			b.append(this.getKeyFilter(r).getWhereClause());
		}
		return b.toString();
	}
	
	// INSERT support
	
	private String getINSERT(DBRecord r) throws SQLException{
		StringBuffer b = new StringBuffer(1024);
		StringBuffer b2 = new StringBuffer(1024);
		for(Iterator i = this.columnDefinitions(r); i.hasNext();){
			DBColumn c = (DBColumn)i.next();
			b.append(((b.length() == 0) ? "" : ", ") + c.getCompatibleName());
			b2.append(((b2.length() == 0) ? "" : ", ") + c.getSQL(r));
		}
		if (b.length() != 0){
			b.insert(0, "INSERT INTO " + this.getCompatibleName() + " (");
			b.append(") VALUES (" + b2 + ")");
		}
		return b.toString();
	}
	
	// DELETE support
	
	private String getDELETE(DBRecord r) throws SQLException{
		StringBuffer b = new StringBuffer(1024);
		b.append("DELETE FROM " + this.getCompatibleName() + getKeyFilter(r).getWhereClause());
		return b.toString();
	}
	
	// iterator support
	
	protected Statement stmt;
	protected ResultSet rs;
	protected boolean more;
	
	public void init() throws SQLException{
		stmt = m_con.createStatement();
		this.setSQL(this.getSELECT());
		rs = stmt.executeQuery(this.getSQL());
		more = rs.next();
	}
	
	public void init(DBFilter dbf) throws SQLException{
		stmt = m_con.createStatement();
		this.setSQL(this.getSELECT() + dbf.getWhereClause() + dbf.getOrderByClause());
		rs = stmt.executeQuery(this.getSQL());
		more = rs.next();
	}
	
	public boolean more(){
		return more;
	}
	
	public DBRecord next() throws SQLException{
		DBRecord r = null;
		if(more){
			DBRecordBuffer buffer = new DBRecordBuffer(this, rs);
			if(this.getSelector() == null){
				r = this.getPattern().duplicate();
				buffer.build(r, ' ');
			}else{
				String cl = (String)buffer.get(this.getSelector().getFieldName());
				r = this.getPattern(cl).duplicate();
				buffer.build(cl, r, ' ');
			}
			this.validate(r);
			r.setQueried();
			r.addObserver(this);
			more = rs.next();
			if(!more){
				rs.close();
				stmt.close();
			}
		}
		return r;
	}
	
	// SQL execution & translation
	
	private boolean query(DBRecord c) throws SQLException{
		Statement stmt = m_con.createStatement();
		this.setSQL(this.getSELECT(c));
		ResultSet rs = stmt.executeQuery(this.getSQL());
		boolean found = false;
		if (rs.next()){
			DBRecordBuffer buffer = new DBRecordBuffer(this, rs);
			Object cl = (this.getSelector() == null) ? null : buffer.get(this.getSelector().getFieldName());
			buffer.build(cl, c, 'N');
			this.validate(c);
			c.addObserver(this);
			found = true;
		}
		rs.close();
		stmt.close();
		return found;
	}
	
	private void update(DBPattern dbp, DBFilter dbf) throws SQLException{
		Statement stmt = m_con.createStatement();
		this.setSQL(this.getUPDATE(dbp, dbf));
		try{
			stmt.executeUpdate(this.getSQL());
			this.setChanged();
		}catch(SQLException ex){
			throw new SQLException("Cannot update table.");
		}finally{
			stmt.close();
		}		
	}
	
	private void update(DBRecord c) throws SQLException{
		c.setTable(this);
		Statement stmt = m_con.createStatement();
		this.setSQL(getUPDATE(c));
		try{
			stmt.executeUpdate(this.getSQL());
			this.setChanged();
		}catch(SQLException ex){
			throw new SQLException("Cannot update record with key " + c.getKey() + ".");
		}finally{
			stmt.close();
		}
	}

	private void insert(DBRecord c) throws SQLException{
		c.setTable(this);
		Statement stmt = m_con.createStatement();
		this.setSQL(getINSERT(c));
		try{
			stmt.executeUpdate(this.getSQL());
			this.setChanged();
		}catch(SQLException ex){
			throw new SQLException("Cannot insert record with key " + c.getKey() + ".");
		}finally{
			stmt.close();
		}
	}
	
	private void delete(DBRecord c) throws SQLException{
		c.setTable(this);
		Statement stmt = m_con.createStatement();
		this.setSQL(this.getDELETE(c));
		try{
			stmt.executeUpdate(this.getSQL());
			this.setChanged();
		}catch(SQLException ex){
			throw new SQLException("Cannot delete record with key " + c.getKey() + ".");
		}finally{
			stmt.close();
		}
	}
	
	public int size() throws SQLException{
		return size(null);
	}
	
	public int size(DBFilter dbf) throws SQLException{
		Statement stmt = m_con.createStatement();
		this.setSQL("SELECT COUNT(*) FROM " + this.getCompatibleName() + ((dbf != null) ? dbf.getWhereClause() : ""));
		ResultSet rs = stmt.executeQuery(this.getSQL());
		int count = 0;
		if (rs.next()){
			count = rs.getInt(1);
		}
		rs.close();
		stmt.close();
		return count;
	}
	
	public void clear() throws SQLException{
		Statement stmt = m_con.createStatement();
		this.setSQL("DELETE FROM " + this.getCompatibleName());
		stmt.executeUpdate(this.getSQL());
		stmt.close();
		setChanged();
	}
	
	public void clear(DBFilter dbf) throws SQLException{
		Statement stmt = m_con.createStatement();
		this.setSQL("DELETE FROM " + this.getCompatibleName() + dbf.getWhereClause());
		stmt.executeUpdate(this.getSQL());
		stmt.close();
		setChanged();
	}
	
	
	// Observer
	
	public void update(Observable o, Object arg){
		setChanged();
	}
	
	// miscellaneous
	
	public void checkStructure() throws SQLException{
		for(Iterator i = this.columnDefinitions(); i.hasNext();){
			DBColumn dbc = (DBColumn)i.next();
			String colName = dbc.getCompatibleName();
			Statement stmt = m_con.createStatement();
			try{
				this.setSQL("SELECT " + colName + " FROM " + this.getCompatibleName());
				ResultSet rs = stmt.executeQuery(this.getSQL());
			}catch(SQLException ex){
				throw new SQLException("Column " + colName + " of table " + this.getCompatibleName() + " not found");
			}finally{
				stmt.close();
			}
		}
	}
	
	// to be overloaded
	
	public abstract void validate(DBRecord r) throws SQLException;
	
	// and the rest
	
	public DBRecord find(DBRecord dbr) throws SQLException{
		DBRecord r = null;
		Statement stmt = m_con.createStatement();
		this.setSQL(getSELECT(dbr));
		ResultSet rs = stmt.executeQuery(this.getSQL());
		if (rs.next()){
			DBRecordBuffer buffer = new DBRecordBuffer(this, rs);
			if(this.getSelector() == null){
				r = this.getPattern().duplicate();
				buffer.build(r, ' ');
			}else{
				DBColumn c = this.getSelector();
				Object cl = buffer.get(this.getSelector().getFieldName());
				r = this.getPattern(cl).duplicate();
				buffer.build(cl, r, ' ');
			}
			r.setQueried();
			r.clearModified();
			this.validate(r);
			r.addObserver(this);
		}
		rs.close();
		stmt.close();
		return r;
	}
	
	public boolean set(DBPattern dbp) throws SQLException{
		return this.set(dbp, null);
	}
	
	public boolean set(DBPattern dbp, DBFilter dbf) throws SQLException{
		this.update(dbp, dbf);
		return true;
	}
	
	public boolean load(DBRecord dbr) throws SQLException{
		boolean retVal = this.query(dbr);
		if(retVal){
			dbr.setQueried();
			dbr.clearModified();
		}
		return retVal;
	}
	
	public boolean save(DBRecord dbr) throws SQLException{
		if(dbr.isModified()){
			if(dbr.isQueried()) this.update(dbr);
			else insert(dbr);
		}
		dbr.setQueried();
		dbr.clearModified();
		return true;
	}
	
	public boolean erase(DBRecord dbr) throws SQLException{
		if(dbr.isQueried()){
			this.delete(dbr);
			dbr.clearQueried();
			dbr.setModified();
			return true;
		}
		return false;
	}
	
	public String getKey(DBRecord r) throws SQLException{
		StringBuffer s = new StringBuffer(1024);
		for(Iterator i = this.columnDefinitions(); i.hasNext();){
			DBColumn c = (DBColumn)i.next();
			if (c.isPrimaryKey()) s.append(c.getSQL(r));
		}
		return s.toString();
	}
	
	
}