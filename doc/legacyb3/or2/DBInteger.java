package legacyb3.or2;

public class DBInteger extends DBField {
	
	public static final DBInteger INTEGER = new DBInteger();
	
	public DBInteger(){
		super(Integer.TYPE);
	}
	
	public Object toUser(Object o){
		return (Integer)o;
	}
	
	public Object toJDBC(Object o){
		return (Integer)o;
	}
	
	public String toSQL(Object o){
		return ((Integer)o).toString();
	}


}
