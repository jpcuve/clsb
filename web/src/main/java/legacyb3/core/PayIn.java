package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import legacyb3.util.*;
import legacyb3.data.*;

import java.util.Enumeration;
import java.util.Vector;

public class PayIn extends ClsObject implements  Bookable  {
	
	private Account ac;
	private Currency ccy;
	private String status;
	private TimeOfDay tod;
	private double am;
	
	public PayIn(){
		this(null, null, null, null, 0);
	}
	
	public PayIn(Account ac, Currency ccy, String status, TimeOfDay tod, double am){
		setAccount(ac);
		setCurrency(ccy);
		setStatus(status);
		setTimeOfDay(tod);
		setAmount(am);
	}
	
	public void setAccount(Account ac){
		this.ac = ac;
	}
	
	public Account getAccount(){
		return this.ac;
	}
	
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public Currency getCurrency(){
		return this.ccy;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setTimeOfDay(TimeOfDay tod){
		this.tod = tod;
	}
	
	public TimeOfDay getTimeOfDay(){
		return this.tod;
	}
	
	public void setAmount(double am){
		this.am = am;
	}
	
	public double getAmount(){
		return this.am;
	}
	
	public String getReference(){
		return "PI" + tod;
	}
	
	// Bookable
	
	public Enumeration movements(){
		Vector v = new Vector();
		v.addElement(new Movement(Movement.makeReference("MV"), this.getReference(), MovementType.in, ac, ccy.getMirrorAccount(), ccy, am));
		v.addElement(new Movement(Movement.makeReference("MV"), this.getReference(), MovementType.pi, ccy.getMirrorAccount(), ac, ccy, am));
		return v.elements();
	}
	
	// JAVA support
	
	public String toString(){
		return "[ac=" + ac + ", ccy=" + ccy + ", st=" + status + ", tod=" + tod + ", am=" + am + "]";
	}
	
	public Object clone(){
		PayIn pi = (PayIn)super.clone();
		return pi;
	}
	
}