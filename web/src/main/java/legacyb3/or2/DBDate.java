package legacyb3.or2;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DBDate extends DBField {
	private SimpleDateFormat m_df;
	
	public static final DBDate DATE = new DBDate();
	
	public DBDate(){
		super(Date.class);
		m_df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	}
	
	public Object toUser(Object o){
		return (Date)o;
	}
	
	public Object toJDBC(Object o){
		return (java.sql.Timestamp)o;
	}
	
	public String toSQL(Object o){
		Date d = (Date)o;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return "#" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR) + "#";
	}
	

}
