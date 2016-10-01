package legacyb3.data;

import java.sql.*;
import legacyb3.or2.*;
import legacyb3.core.*;

public class CurrencyGroupFK extends DBField {
	private CurrencyGroups m_cgt;
	private static CurrencyGroupFK CURRENCY_GROUP = null;
	
	public static CurrencyGroupFK instance(CurrencyGroups cgt){
		if(CURRENCY_GROUP == null) CURRENCY_GROUP = new CurrencyGroupFK(cgt);
		return CURRENCY_GROUP;
	}
	
	private CurrencyGroupFK(CurrencyGroups cgt){
		super(CurrencyGroup.class);
		m_cgt = cgt;
	}
	
	public Object toUser(Object o){
		CurrencyGroup cg = null;
		try{
			cg = m_cgt.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return cg;
	}
	
	public Object toJDBC(Object o){
		return ((CurrencyGroup)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((CurrencyGroup)o).getID() + "'";
	}
}
