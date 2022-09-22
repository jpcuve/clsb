package legacyb3.or2;

import java.lang.reflect.Method;
import java.sql.SQLException;

public class DBColumn{
	private DBTable m_table;
	private boolean m_primaryKey;
	private DBField m_type;
	private String m_fieldName;
	private String m_propertyName;
	
	public DBColumn(DBTable t, boolean pKey, DBField type, String fName, String pName){
		m_table = t;
		m_primaryKey = pKey;
		m_type = type;
		m_fieldName = fName;
		m_propertyName = pName;
	}
	
	public boolean isPrimaryKey(){
		return m_primaryKey;
	}
	
	public String getFieldName(){
		return m_fieldName;
	}
	
	public DBField getField(){
		return m_type;
	}
	
	public String getCompatibleName(){
		return (m_fieldName.indexOf(" ") == -1) ? m_fieldName : ("[" + m_fieldName + "]");
	}
	
	public void setProperty(DBRecord r, Object jdbc) throws SQLException {
		try{
			Class[] cl = { m_type.getType() };
			Object[] o = { m_type.toUser(jdbc) };
			Method m = null;
			if(m_type != DBBoolean.BOOLEAN){
				m = r.getClass().getMethod("set" + m_propertyName, cl);
				m.invoke(r, o);
			}else{
				m = (((Boolean)o[0]).booleanValue()) ? r.getClass().getMethod("set" + m_propertyName,(Class[]) null) : r.getClass().getMethod("clear" + m_propertyName,(Class[]) null);
				m.invoke(r, (Object[])null);
			}
		}catch(Throwable ex){
			throw new SQLException(r.getClass().getName() + " " + ex.toString());
		}
	}
	
	private Object get(DBRecord r) throws SQLException {
		Object o = null;
		try{
			Method m = null;
			if(m_type != DBBoolean.BOOLEAN){
				m = r.getClass().getMethod("get" + m_propertyName, (Class[])null);
			}else{
				m = r.getClass().getMethod("is" + m_propertyName, (Class[])null);
			}
			o = m.invoke(r, (Object[])null);
		}catch(Throwable ex){
			throw new SQLException(r.getClass().getName() + " " + ex.toString());
		}
		return o;
	}
	
	public Object getProperty(DBRecord r) throws SQLException {
		Object o = this.get(r);
		return (o == null) ? null : m_type.toJDBC(o);
	}
		
	public String getSQL(DBRecord r) throws SQLException {
		Object o = this.get(r);
		return (o == null) ? null : m_type.toSQL(o);
	}
		
	// JAVA support
	
	public int hashCode(){
		return m_fieldName.hashCode();
	}
	
	public boolean equals(Object o){
		DBColumn dbc = (DBColumn)o;
		return m_fieldName.equals(dbc.m_fieldName);
	}
}