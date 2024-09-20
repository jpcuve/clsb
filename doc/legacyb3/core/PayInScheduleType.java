package legacyb3.core;


public class PayInScheduleType{
	public final static String CURRENT = "CURR";
	public final static String PREVIOUS = "PREV";
	public final static String INITIAL = "INIT";
}

/*
public class PayInScheduleType extends ClsObject implements Cloneable, InterfaceID, InterfaceName {
	private String id;
	private String name;
	
	public static PayInScheduleType init;
	public static PayInScheduleType prev;
	public static PayInScheduleType curr;
	
	
	// constructors
	
	public PayInScheduleType(String id){
		this(id, null);
	}
	
	public PayInScheduleType(String id, String name){
		setID(id);
		setName(name);
	}
	
	// accessors
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	// JAVA support
	
	public String toString(){
		return "[id=" + this.getID() + ", name=" + this.getName() + "]";
	}
	
	public Object clone(){ // shallow
		PayInScheduleType cg = (PayInScheduleType)super.clone();
		return cg;
	}
	
}
*/