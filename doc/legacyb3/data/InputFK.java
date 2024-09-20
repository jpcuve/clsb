package legacyb3.data;

import legacyb3.core.Input;
import legacyb3.or2.DBField;

public class InputFK extends DBField {
	private static InputFK INPUT = null;
	
	public static InputFK instance(){
		if(INPUT == null) INPUT = new InputFK();
		return INPUT;
	}
	
	private InputFK(){
		super(Input.class);
	}
	
	public Object toUser(Object o){
		return null;
	}
	
	public Object toJDBC(Object o){
		return ((Input)o).getReference();
	}
	
	public String toSQL(Object o){
		return "'" + ((Input)o).getReference() + "'";
	}

}
