package legacyb3.core;

import java.sql.SQLException;
import java.text.*;
import legacyb3.data.*;
import legacyb3.util.*;

public class PayInSchedule extends ClsObject {
	private Account ac;
	private Currency ccy;
	private PayInDeadline tod;
	private String pisTyp;
	private double amount;
	
	public PayInSchedule(){
		this(null, null, null, null, 0);
	}
	
	public PayInSchedule(Account ac, Currency ccy, PayInDeadline tod, String pisTyp, double am){
		setAccount(ac);
		setCurrency(ccy);
		setPayInDeadline(tod);
		setPayInScheduleType(pisTyp);
		setAmount(am);
	}
	
	public void setAccount(Account ac){
		this.ac = ac;
	}
	
	public Account getAccount(){
		return ac;
	}
	
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public Currency getCurrency(){
		return ccy;
	}	
	
	public void setPayInDeadline(PayInDeadline tod){
		this.tod = tod;
	}
	
	public PayInDeadline getPayInDeadline(){
		return tod;
	}
	
	public void setPayInScheduleType(String pisTyp){
		this.pisTyp = pisTyp;
	}
	
	public String getPayInScheduleType(){
		return this.pisTyp;
	}
		
	public void setAmount(double am){
		amount = am;
	}
	
	public double getAmount(){
		return amount;
	}
	
	// JAVA support
	
	public String toString(){
		return "[ac=" + ac.getID() + ", ccy=" + ccy.getID() + ", pid=" + tod.getID() + "pist=" + pisTyp + ", am=" + amount + "]";
	}
	
	public Object clone(){
		PayInSchedule pis = (PayInSchedule)super.clone();
		return pis;
	}

}