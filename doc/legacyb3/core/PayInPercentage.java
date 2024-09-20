package legacyb3.core;

import legacyb3.or2.TimeOfDay;

public class PayInPercentage extends ClsObject { 
	 
	private Currency ccy;
	private PayInDeadline pid;
	private double perc;
	
	public PayInPercentage(Currency ccy, PayInDeadline pid, double perc){
		this.setCurrency(ccy);
		this.setPayInDeadline(pid);
		this.setPercentage(perc);
	}
	
	public PayInPercentage(){
		this(null, null, 0);
	}
	
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public Currency getCurrency(){
		return this.ccy;
	}
	
	public void setPayInDeadline(PayInDeadline pid){
		this.pid = pid;
	}
	
	public PayInDeadline getPayInDeadline(){
		return this.pid;
	}
	
	public void setPercentage(double perc){
		this.perc = perc;
	}
	
	public double getPercentage(){
		return this.perc;
	}
	
	// miscellaneous
	
	public TimeOfDay getTimeOfDay(){
		return this.getPayInDeadline().getTimeOfDay();
	}
	
	// JAVA support
	
	public Object clone(){
		PayInPercentage pip = (PayInPercentage)super.clone();
		return pip;
	}
	
	public String toString(){
		return "[ccy=" + this.getCurrency().getID() + ", pid=" + this.getPayInDeadline().getID() + "(" + this.getTimeOfDay() + "), perc=" + this.getPercentage() + "]";
	}
}
