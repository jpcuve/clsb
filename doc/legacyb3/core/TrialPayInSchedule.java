package legacyb3.core;

public class TrialPayInSchedule extends ClsObject {
	private Account ac;
	private Currency ccy;
	private PayInDeadline tod;
	private double amount;
	
	public TrialPayInSchedule(){
		this(null, null, null, 0);
	}
	
	public TrialPayInSchedule(Account ac, Currency ccy, PayInDeadline tod, double am){
		setAccount(ac);
		setCurrency(ccy);
		setPayInDeadline(tod);
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
	
	public void setAmount(double am){
		amount = am;
	}
	
	public double getAmount(){
		return amount;
	}
	
	// JAVA support
	
	public String toString(){
		return "[ac=" + ac.getID() + ", ccy=" + ccy.getID() + ", pid=" + tod.getID() + ", am=" + amount + "]";
	}
	
	public Object clone(){
		TrialPayInSchedule pis = (TrialPayInSchedule)super.clone();
		return pis;
	}

}