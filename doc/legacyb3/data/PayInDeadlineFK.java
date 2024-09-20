package legacyb3.data;

import legacyb3.core.PayInDeadline;
import legacyb3.or2.DBField;

import java.sql.SQLException;

public class PayInDeadlineFK extends DBField {
	private PayInDeadlines m_pidt;
	private static PayInDeadlineFK PAY_IN_DEADLINE = null;
	
	public static PayInDeadlineFK instance(PayInDeadlines pidt){
		if(PAY_IN_DEADLINE == null) PAY_IN_DEADLINE = new PayInDeadlineFK(pidt);
		return PAY_IN_DEADLINE;
	}
	
	private PayInDeadlineFK(PayInDeadlines pidt){
		super(PayInDeadline.class);
		m_pidt = pidt;
	}
	
	public Object toUser(Object o){
		PayInDeadline pid = null;
		try{
			pid = m_pidt.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return pid;
	}
	
	public Object toJDBC(Object o){
		return ((PayInDeadline)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((PayInDeadline)o).getID() + "'";
	}
}
