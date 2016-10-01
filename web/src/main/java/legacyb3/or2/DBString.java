package legacyb3.or2;

public class DBString extends DBField {
	
	public static final DBString STRING = new DBString();
	
	public DBString(){
		super(String.class);
	}
	
	public Object toUser(Object o){
		return (String)o;
	}
	
	public Object toJDBC(Object o){
		return (String)o;
	}
	
	public String toSQL(Object o){
		return "'" + (String)o + "'";
	}
}
