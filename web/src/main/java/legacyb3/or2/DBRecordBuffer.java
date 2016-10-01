package legacyb3.or2;

import java.sql.*;
import java.util.Iterator;
import java.util.TreeMap;

public class DBRecordBuffer extends TreeMap {
	private DBTable table;
	
	public DBRecordBuffer(DBTable t, ResultSet rs) throws SQLException {
		this.table = t;
		for(Iterator i = table.columnDefinitions(); i.hasNext();){
			DBColumn dbc = (DBColumn)i.next();
			String col = dbc.getFieldName();
			Object val = rs.getObject(col);
			if(val != null) this.put(col, val);
		}
	}
	
	public void build(DBRecord r, char type) throws SQLException{
		this.build(null, r, type);
	}
	
	public void build(Object cl, DBRecord r, char type) throws SQLException{
		for(Iterator i = table.columnDefinitions(cl); i.hasNext();){
			DBColumn c = (DBColumn)i.next();
			String col = c.getFieldName();
			Object val = this.get(col);
			switch(type){
			case 'K': if(c.isPrimaryKey()) c.setProperty(r, val); break;
			case 'N': if(!c.isPrimaryKey()) c.setProperty(r , val); break;
			default: c.setProperty(r, val); break;
			}
		}
		r.setTable(table);
	}

}