package legacyb3.core;

public class Balance extends ClsObject {
	private Account ac;
	private BalanceType bt;
	private Currency ccy;
	private double amount;
	
	// constructors

	public Balance(Account ac, BalanceType bt, Currency c, double am){
		setAccount(ac);
		setBalanceType(bt);
		setCurrency(c);
		setAmount(am);
	}
	
	public Balance(){
		this(null, null, null, 0);
	}
	
	// accessors
	
	public void setAccount(Account ac){
		this.ac = ac;
	}
	
	public Account getAccount(){
		return ac;
	}
	
	public void setBalanceType(BalanceType bt){
		this.bt = bt;
	}
	
	public BalanceType getBalanceType(){
		return bt;
	}
		
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public Currency getCurrency(){
		return ccy;
	}	
	
	public void setAmount(double am){
		amount = am;
		setChanged(); // everything is trapped into Account class
	}
	
	public double getAmount(){
		return ccy.round(amount);
	}
	
	// JAVA support
	
	public String toString(){
		return "[ac=" + ac.getID() + ", ccy=" + ccy.getID() + ", typ=" + bt.getID()+ ", am=" + amount + "]";
	}
	
	public Object clone(){
		Balance b = (Balance)super.clone();
		return b;
	}

	// miscellaneous
	
	public double post(double am){
		setAmount(getAmount() + am);
		return amount;
	}
	
}