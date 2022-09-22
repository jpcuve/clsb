package legacyb3.data;

import legacyb3.core.AccountType;
import legacyb3.or2.DBField;

import java.sql.SQLException;

public class AccountTypeFK extends DBField {
	private AccountTypes m_att;
	private static AccountTypeFK ACCOUNT_TYPE = null;
	
	public static AccountTypeFK instance(AccountTypes att){
		if(ACCOUNT_TYPE == null) ACCOUNT_TYPE = new AccountTypeFK(att);
		return ACCOUNT_TYPE;
	}
	
	private AccountTypeFK(AccountTypes att){
		super(AccountType.class);
		m_att = att;
	}
	
	public Object toUser(Object o){
		AccountType at = null;
		try{
			at = m_att.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return at;
	}
	
	public Object toJDBC(Object o){
		return ((AccountType)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((AccountType)o).getID() + "'";
	}
}
