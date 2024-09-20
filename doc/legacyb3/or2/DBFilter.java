package legacyb3.or2;


import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class DBFilter {
	private DBTable t;
	private HashMap wheres;
	private TreeMap orderbys;
	
	private class DBColumnOrder{
		private DBColumn dbc;
		private int sortOrder;
		private boolean dir;
		
		public DBColumnOrder(DBColumn dbc, int sortOrder, boolean dir){
			this.dbc = dbc;
			this.sortOrder = sortOrder;
			this.dir = dir;
		}
		
		public DBColumn getColumn(){
			return this.dbc;
		}
		
		public int getSortOrder(){
			return this.sortOrder;
		}
		
		public boolean getDir(){
			return this.dir;
		}
	}

	public DBFilter(DBTable t){
		this.t = t;
		wheres = new HashMap();
		orderbys = new TreeMap();
	}
	
	public DBTable getTable(){
		return this.t;
	}
	
	public boolean setConstraint(String colName, String constraint){
		DBColumn dbc = t.getColumnDefinition(colName);
		if(dbc != null){
			wheres.put(dbc, constraint);
			return true;
		}
		return false;
	}
	
	public boolean setConstraint(DBColumn dbc, String constraint){
		if(t.isColumnDefinition(dbc)){
			wheres.put(dbc, constraint);
			return true;
		}
		return false;
	}
	
	public String getConstraint(DBColumn dbc){
		return (String)wheres.get(dbc);
	}
	
	public boolean setOrder(String colName, int sortOrder, boolean dir){
		DBColumn dbc = t.getColumnDefinition(colName);
		if(dbc != null){
			DBColumnOrder dbco = new DBColumnOrder(dbc, sortOrder, dir);
			orderbys.put(new Integer(dbco.getSortOrder()), dbco);
			return true;
		}
		return false;
	}
	
	public boolean setOrder(DBColumn dbc, int sortOrder, boolean dir){
		if(t.isColumnDefinition(dbc)){
			DBColumnOrder dbco = new DBColumnOrder(dbc, sortOrder, dir);
			orderbys.put(new Integer(dbco.getSortOrder()), dbco);
			return true;
		}
		return false;
	}
	
	public int getOrder(DBColumn dbc){
		return 0;
	}
	
	public boolean getDir(DBColumn dbc){
		return false;
	}
		
	public String getWhereClause(){
		StringBuffer whereClause = new StringBuffer(1024);
		for(Iterator i = wheres.keySet().iterator(); i.hasNext();){
			DBColumn dbc = (DBColumn)i.next();
			String cons = (String)wheres.get(dbc);
			if (cons != null && cons != ""){
				whereClause.append((whereClause.length() == 0) ? " WHERE " : " AND ");
				whereClause.append(dbc.getCompatibleName() + cons);
			}
		}
		return whereClause.toString();
	}
	
	public String getOrderByClause(){
		StringBuffer orderByClause = new StringBuffer(1024);
		for(Iterator i = orderbys.values().iterator(); i.hasNext();){
			DBColumnOrder dbco = (DBColumnOrder)i.next();
			String dir = dbco.getDir() ? "" : "DESC";
			orderByClause.append((orderByClause.length() == 0) ? " ORDER BY " : " , ");
			orderByClause.append(dbco.getColumn().getCompatibleName() + " " + dir);
		}
		return orderByClause.toString();
	}
}
