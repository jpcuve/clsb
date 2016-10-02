package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import legacyb3.data.*;

public class RTGSCutOff extends ClsObject { 
	
	private Currency ccy;
	private TimeOfDay start;
	private TimeOfDay end;
	
	public RTGSCutOff(){
		this(null, null, null);
	}
	
	public RTGSCutOff(Currency ccy, TimeOfDay start, TimeOfDay end){
		setCurrency(ccy);
		setStart(start);
		setEnd(end);
	}
	
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public Currency getCurrency(){
		return this.ccy;
	}
	
	public void setStart(TimeOfDay start){
		this.start = start;
	}
	
	public TimeOfDay getStart(){
		return this.start;
	}
	
	public void setEnd(TimeOfDay end){
		this.end = end;
	}
	
	public TimeOfDay getEnd(){
		return this.end;
	}
	
	// JAVA support
	
	public String toString(){
		return "[ccy=" + ccy + ", start=" + start + ", end=" + end + "]";
	}
	
	public Object clone(){
		RTGSCutOff rco = (RTGSCutOff)super.clone();
		return rco;
	}
	

}
