package legacyb1;

public class PayInSchedule extends DBRecord{
	private Account ac;
	private Currency ccy;
	private TimeOfDay tod;
	private double amount;
	
	// private class
	
	public PayInSchedule(){
		this(null, null, null,0);
	}
	
	public PayInSchedule(Account ac, Currency ccy, TimeOfDay tod, double am){
		setAccount(ac);
		setCurrency(ccy);
		setTime(tod);
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
	
	public void setTime(TimeOfDay tod){
		this.tod = tod;
	}
	
	public TimeOfDay getTime(){
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
		return "[ac=" + ac.getName() + ", ccy=" + ccy.getName() + ", tod=" + tod + ", am=" + amount + "]";
	}
	
	public Object clone(){
		return new PayInSchedule(ac, ccy, tod, amount);
	}

}