package legacyb3.or2;

import java.sql.*;

public class DBRecordIterator implements DBEnumeration{
	private DBTable table;
	
	public DBRecordIterator(DBTable t) throws SQLException{
		table = t;
		table.init();
	}
	
	public DBRecordIterator(DBFilter dbf) throws SQLException{
		table = dbf.getTable();
		table.init(dbf);
	}
	
	public boolean hasMoreElements(){
		return table.more();
	}
	
	public Object nextElement() throws SQLException {
		DBRecord dbr = null;
		dbr = table.next();
		return dbr;
	}
}