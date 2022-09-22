package legacyb3.data;

import legacyb3.core.MovementType;
import legacyb3.or2.DBField;

import java.sql.SQLException;

public class MovementTypeFK extends DBField {
	private MovementTypes m_mtt;
	private static MovementTypeFK MOVEMENT_TYPE = null;
	
	public static MovementTypeFK instance(MovementTypes mtt){
		if(MOVEMENT_TYPE == null) MOVEMENT_TYPE = new MovementTypeFK(mtt);
		return MOVEMENT_TYPE;
	}
	
	private MovementTypeFK(MovementTypes mtt){
		super(MovementType.class);
		m_mtt = mtt;
	}
	
	public Object toUser(Object o){
		MovementType mt = null;
		try{
			mt = m_mtt.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return mt;
	}
	
	public Object toJDBC(Object o){
		return ((MovementType)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((MovementType)o).getID() + "'";
	}
}
