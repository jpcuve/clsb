package legacyb3.or2;

import java.sql.*;
import java.util.Iterator;
import java.util.TreeMap;

public abstract class DBCachedTable extends DBTable{
	private TreeMap recs;
	
	// constructors
	
	public DBCachedTable(Connection c, String s, DBRecord p){
		super(c, s, p);
		this.initialize();
	}

	private void initialize() {
		this.recs = new TreeMap();
	}
	
	// miscellaneous
	
	protected void add(DBRecord r) throws SQLException {
		this.recs.put(r.getKey(), r);
	}
	
	protected void remove(DBRecord r) throws SQLException {
		super.save(r);
		this.recs.remove(r.getKey());
	}
	
	public void flush() throws SQLException {
		for(Iterator i1 = this.recs.values().iterator(); i1.hasNext();){
			DBRecord r = (DBRecord)i1.next();
			super.save(r);
		}
	}
	
	public void clear() throws SQLException{
		this.recs.clear();
		super.clear();
	}
	
	public void clear(DBFilter dbf) throws SQLException{
		this.flush();
		for(DBEnumeration e1 = this.records(dbf); e1.hasMoreElements();){
			DBRecord dbr = (DBRecord)e1.nextElement();
			this.erase(dbr);
		}
	}
	
	public void load() throws SQLException {
		this.clear();
		for(DBEnumeration e1 = this.records(); e1.hasMoreElements();) e1.nextElement();
	}
	
	public void save() throws SQLException{
		this.flush();
		this.notifyObservers();
	}
	
	// iterator support

	public DBRecord next() throws SQLException {
		DBRecord r = null;
		if(more){
			DBRecordBuffer buffer = new DBRecordBuffer(this, rs);
			if(this.getSelector() == null){
				r = this.getPattern().duplicate();
				buffer.build(r, 'K');
				String key = r.getKey();
				DBRecord s = (DBRecord)this.recs.get(key);
				if(s != null){
					r = s;
				}else{
					buffer.build(r, 'N');
					this.validate(r);
					this.add(r);
					r.clearModified();
				}
			}else{
				Object cl = buffer.get(this.getSelector().getFieldName());
				r = this.getPattern(cl).duplicate();
				buffer.build(cl, r, 'K');
				String key = r.getKey();
				DBRecord s = (DBRecord)this.recs.get(key);
				if(s != null){
					r = s;
				}else{
					buffer.build(cl, r, 'N');
					this.validate(r);
					this.add(r);
					r.clearModified();
				}
			}
			r.setQueried();
			more = rs.next();
			if(!more){
				rs.close();
				stmt.close();
			}
		}
		return r;
	}
	
	// miscellaneous

	
	public boolean set(DBPattern dbp) throws SQLException{
		return set(dbp, null);
	}
	
	public boolean set(DBPattern dbp, DBFilter dbf) throws SQLException{
		throw new SQLException("Cannot sweep update cached records due to uninterpreted WHERE clause. Update records individually.");
	}
	
	public DBRecord find(DBRecord dbr) throws SQLException{
		dbr.setTable(this);
		String key = dbr.getKey();
		DBRecord r = (DBRecord)this.recs.get(key);
		if(r == null){
			r = super.find(dbr);
			if(r != null){
				this.add(r);
				r.clearModified();
				r.setQueried();
			}
		}
		return r;
	}
	
	public boolean load(DBRecord dbr) throws SQLException{
		dbr.setTable(this);
		String key = dbr.getKey();
		boolean bRet = false;
		DBRecord r = (DBRecord)this.recs.get(key);
		if(r != null){ // found in the cache
			for(Iterator i = columnDefinitions(r); i.hasNext();){
				DBColumn c = (DBColumn)i.next();
				if(!c.isPrimaryKey()) c.setProperty(dbr, c.getProperty(r));
				
			}
			dbr.setQueried();
			bRet = true;
		}else{ // not found in the cache
			bRet = super.load(dbr);
			if(bRet == true){
				this.add(dbr);
				dbr.setQueried();
				dbr.clearModified();
			}else{
				dbr.clearQueried();
			}
		}
		return bRet;
	}
	
	public boolean save(DBRecord dbr) throws SQLException{
		dbr.setTable(this);
		this.setChanged();
		String key = dbr.getKey();
		DBRecord r = (DBRecord)this.recs.get(key);
		if(r == null){ // not found in the cache
			this.add(dbr);
		}	
		return true;
	}
	
	public boolean erase(DBRecord dbr) throws SQLException{
		String key = dbr.getKey();
		DBRecord r = (DBRecord)this.recs.get(key);
		if(r != null){ // found in cache
			this.recs.remove(r.getKey());
		}
		return super.erase(dbr);
	}

}
