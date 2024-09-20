package legacyb3.data;

import legacyb3.core.Account;
import legacyb3.or2.DBField;

import java.sql.SQLException;

public class AccountFK extends DBField {
	private Accounts m_at;
	private static AccountFK ACCOUNT = null;
	
	public static AccountFK instance(Accounts at){
		if(ACCOUNT == null) ACCOUNT = new AccountFK(at);
		return ACCOUNT;
	}
	
	private AccountFK(Accounts at){
		super(Account.class);
		m_at = at;
	}
	
	public Object toUser(Object o){
		Account a = null;
		try{
			a = m_at.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return a;
	}
	
	public Object toJDBC(Object o){
		return ((Account)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((Account)o).getID() + "'";
	}
}
