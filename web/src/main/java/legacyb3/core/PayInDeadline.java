package legacyb3.core;

import legacyb3.or2.*;
import legacyb3.data.*;

public class PayInDeadline extends ClsObject {
	
	private String id;
	private CurrencyGroup cg;
	private TimeOfDay tod;
	
	public PayInDeadline(String id, CurrencyGroup cg, TimeOfDay tod){
		this.setID(id);
		this.setCurrencyGroup(cg);
		this.setTimeOfDay(tod);
	}
	
	public PayInDeadline(){
		this(null, null, null);
	}
	
	public PayInDeadline(String id){
		this(id, null, null);
	}
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID(){
		return this.id;
	}
	
	public void setCurrencyGroup(CurrencyGroup cg){
		this.cg = cg;
	}
	
	public CurrencyGroup getCurrencyGroup(){
		return this.cg;
	}
	
	public void setTimeOfDay(TimeOfDay tod){
		this.tod = tod;
	}
	
	public TimeOfDay getTimeOfDay(){
		return this.tod;
	}
	
	public Object clone(){
		PayInDeadline pid = (PayInDeadline)super.clone();
		return pid;
	}
	
	public String toString(){
		return "[id=" + this.getID() + ", cg=" + this.getCurrencyGroup().getID() + ", tod=" + this.getTimeOfDay() + "]";
	}

}
