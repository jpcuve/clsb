package legacyb3.or2;

public class DBDouble extends DBField {
	
	public static final DBDouble DOUBLE = new DBDouble();
	
	public DBDouble(){
		super(Double.TYPE);
	}
	
	public Object toUser(Object o){
		return (Double)o;
	}
	
	public Object toJDBC(Object o){
		return (Double)o;
	}
	
	public String toSQL(Object o){
		return ((Double)o).toString();
	}


	
}
