package legacyb3.or2;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

public class DBPattern{
	private DBTable table;
	private DBRecord pattern;
	private Vector cols;
	
	public DBPattern(DBTable t, DBRecord r){
		this.setTable(t);
		this.setPattern(r);
		this.cols = new Vector();
	}
	
	public final void setTable(DBTable t){
		this.table = t;
	}
	
	public final void setPattern(DBRecord r){
		this.pattern = r;
	}
	
	public final boolean addColumn(DBColumn dbc){
		if(this.table.isColumnDefinition(dbc)){
			this.cols.addElement(dbc);
			return true;
		}
		return false;
	}
	
	public Enumeration columns(){
		return this.cols.elements();
	}
	
	public final String getSet() throws SQLException{
		StringBuffer set = new StringBuffer(1024);
		for(Enumeration e = this.cols.elements(); e.hasMoreElements();){
			DBColumn dbc = (DBColumn)e.nextElement();
			String cons = dbc.getSQL(this.pattern);
			if (cons != null && cons != ""){
				set.append((set.length() == 0) ? " SET " : " , ");
				set.append(dbc.getCompatibleName() + "=" + cons);
			}
		}
		return set.toString();		
	}
	
}
