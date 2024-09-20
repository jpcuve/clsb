package legacyb3.or2;

public class DBLong extends DBField {
	
	public static final DBLong LONG = new DBLong();
	
	public DBLong(){
		super(Long.TYPE);
	}

	public Object toUser(Object o){
		return new Long(((Integer)o).longValue());
	}
	
	public Object toJDBC(Object o){
		return new Integer(((Long)o).intValue());
	}
	
	public String toSQL(Object o){
		return ((Long)o).toString();
	}

}
