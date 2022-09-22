package legacyb3.data;

import legacyb3.core.BalanceType;
import legacyb3.or2.DBField;

import java.sql.SQLException;

public class BalanceTypeFK extends DBField {
	private BalanceTypes m_btt;
	private static BalanceTypeFK BALANCE_TYPE = null;
	
	public static BalanceTypeFK instance(BalanceTypes btt){
		if(BALANCE_TYPE == null) BALANCE_TYPE = new BalanceTypeFK(btt);
		return BALANCE_TYPE;
	}
	
	private BalanceTypeFK(BalanceTypes btt){
		super(BalanceType.class);
		m_btt = btt;
	}
	
	public Object toUser(Object o){
		BalanceType bt = null;
		try{
			bt = m_btt.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return bt;
	}
	
	public Object toJDBC(Object o){
		return ((BalanceType)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((BalanceType)o).getID() + "'";
	}
}
