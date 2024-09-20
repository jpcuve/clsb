package legacyb3.or2;

import java.sql.SQLException;
import java.util.Observable;

public abstract class DBRecord extends Observable implements Cloneable{
	private DBTable t;
	private boolean queried;
	private boolean modified;
	
	// constructor
	
	public DBRecord(){
		queried = false;
		this.setChanged();
	}
	
	// 
	
	public abstract DBRecord duplicate();
	
	// accessors
	
	public void setTable(DBTable t){
		this.t = t;
	}
	
	public DBTable getTable(){
		return t;
	}
	
	public void clearQueried(){
		this.queried = false;
	}
	
	public void setQueried(){
		this.queried = true;
	}
	
	public boolean isQueried(){
		return this.queried;
	}
	
	public void clearModified(){
		this.modified = false;
	}
	
	public void setModified(){
		this.modified = true;
	}
	
	public boolean isModified(){
		return this.modified;
	}
	
	// Observable overload
	
	public void setChanged(){
		this.setModified();
		super.setChanged();
	}
	
	// JAVA support
	
	public Object clone(){
		DBRecord r = null;
		try{
			r = (DBRecord)super.clone();
			r.setModified();
			r.clearQueried();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return r;
	}
	
	public String getKey() throws SQLException{
		return t.getKey(this);
	}
	
	// miscellaneous
	
	public boolean save() throws SQLException{
		String key = this.getKey();
		boolean saved = false;
		if(t != null) saved = t.save(this);
		else throw new SQLException("Cannot save record " + key + " whose table is unknown. Use setTable().");
		return saved;
	}
	
	public boolean save(DBTable tt) throws SQLException{
		this.t = tt;
		return t.save(this);
	}
	
	public boolean load() throws SQLException{
		boolean loaded = false;
		if(t != null) loaded = t.load(this);
		else throw new SQLException("Cannot load a record whose table is unknown. Use setTable().");
		return loaded;
	}
	
	public boolean load(DBTable tt) throws SQLException{
		this.t = tt;
		return t.load(this);
	}
	
	public boolean erase() throws SQLException{
		boolean erased = false;
		if(t != null) erased = t.erase(this);
		else throw new SQLException("Cannot erase a record whose table is unknown. Use setTable().");
		return erased;
	}
	
	public boolean erase(DBTable tt) throws SQLException{
		this.t = tt;
		return t.erase(this);
	}
	
}