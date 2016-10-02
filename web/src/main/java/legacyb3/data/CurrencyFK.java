package legacyb3.data;

import java.sql.*;
import legacyb3.or2.*;
import legacyb3.core.*;

public class CurrencyFK extends DBField {
	private Currencies m_ct;
	private static CurrencyFK CURRENCY = null;
	
	public static CurrencyFK instance(Currencies ct){
		if(CURRENCY == null) CURRENCY = new CurrencyFK(ct);
		return CURRENCY;
	}
	
	private CurrencyFK(Currencies ct){
		super(Currency.class);
		m_ct = ct;
	}
	
	public Object toUser(Object o){
		Currency c = null;
		try{
			c = m_ct.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return c;
	}
	
	public Object toJDBC(Object o){
		return ((Currency)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((Currency)o).getID() + "'";
	}
}
