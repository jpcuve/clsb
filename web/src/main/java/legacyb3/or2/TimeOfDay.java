package legacyb3.or2;

public class TimeOfDay implements Cloneable {
	public int hour;
	public int min;
	
	public TimeOfDay(){
		setTime(0, 0);
	}
	
	public TimeOfDay(int m){
		setMinutes(m);
	}
	
	public TimeOfDay(int h, int m){
		setTime(h, m);
	}
	
	public void setTime(int h, int m){
		this.hour = h;
		this.min = m;
	}
	
	public int getMinutes(){
		return hour * 60 + min;
	}
	
	public void setMinutes(int m){
		hour = (m / 60) % 24;
		min = m % 60;
	}
	
	public boolean isBefore(TimeOfDay tod){
		return (tod == null) ? false : getMinutes() < tod.getMinutes();
	}
	
	public boolean isBeforeOrEqual(TimeOfDay tod){
		return (tod == null) ? false : getMinutes() <= tod.getMinutes();
	}
	
	public boolean isAfter(TimeOfDay tod){
		return (tod == null) ? false : getMinutes() > tod.getMinutes();
	}
	
	public boolean isAfterOrEqual(TimeOfDay tod){
		return (tod == null) ? false : getMinutes() >= tod.getMinutes();
	}
	
	public boolean equals(Object o){
		if(o != null){
			TimeOfDay tod = (TimeOfDay)o;
			return getMinutes() == tod.getMinutes();
		}
		return false;
	}
	
	public static TimeOfDay add(TimeOfDay tod1, TimeOfDay tod2){
		return new TimeOfDay(tod1.getMinutes() + tod2.getMinutes());
	}
	
	public void add(TimeOfDay tod){
		setMinutes(getMinutes() + tod.getMinutes());
	}
	
	public static TimeOfDay sub(TimeOfDay tod1, TimeOfDay tod2){
		return new TimeOfDay(tod1.getMinutes() + 24 * 60 - tod2.getMinutes());
	}
	
	public void sub(TimeOfDay tod){
		setMinutes(getMinutes() + 24 * 60 - tod.getMinutes());
	}
	
	public String toString(){
		return ((hour < 10) ? "0": "") + hour + ":" + ((min < 10) ? "0" : "") + min;
	}
	
	public Object clone(){
		TimeOfDay tod = null;
		try{
			tod = (TimeOfDay)super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
		}
		return tod;
	}
	
	public int hashCode(){
		return getMinutes();
	}
	
}