package legacyb3.or2;

import java.util.*;
import java.sql.Timestamp;

public class DBTimeOfDay extends DBField {
	
	public static final DBTimeOfDay TIME_OF_DAY = new DBTimeOfDay();
	
	public DBTimeOfDay(){
		super(TimeOfDay.class);
	}
	
	public Object toUser(Object o){
		Calendar c = Calendar.getInstance();
		c.setTime((Timestamp)o);
		return new TimeOfDay(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
	}
	
	public Object toJDBC(Object o){
		TimeOfDay tod = (TimeOfDay)o;
		return new Timestamp(70, 0, 1, tod.hour, tod.min, 0, 0);
	}
	
	public String toSQL(Object o){
		TimeOfDay tod = (TimeOfDay)o;
		return "#" + tod.hour + ":" + tod.min + ":0#";
	}


}
