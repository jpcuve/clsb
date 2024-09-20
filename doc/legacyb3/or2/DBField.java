package legacyb3.or2;

public abstract class DBField {
	private Class m_cl;
	
	public DBField(Class cl){
		m_cl = cl;
	}
	
	public Class getType(){
		return m_cl;
	}
	
	public abstract Object toUser(Object o);
	public abstract Object toJDBC(Object o);
	
	public abstract String toSQL(Object o);
	
}
