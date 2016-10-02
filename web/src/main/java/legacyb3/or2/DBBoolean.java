package legacyb3.or2;

public class DBBoolean extends DBField {
	
	public static final DBBoolean BOOLEAN = new DBBoolean();
	
	public DBBoolean(){
		super(Boolean.TYPE);
	}
	
	public Object toUser(Object o){
		return (Boolean)o;
	}
	
	public Object toJDBC(Object o){
		return (Boolean)o;
	}
	
	public String toSQL(Object o){
		return ((Boolean)o).toString();
	}


}
