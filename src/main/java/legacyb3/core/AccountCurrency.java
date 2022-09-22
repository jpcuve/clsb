package legacyb3.core;

public class AccountCurrency extends ClsObject {
	private Account ac;
	private Currency ccy;
	private boolean neligible;
	private boolean suspendedForPayOut;
	
	// constructors

	public AccountCurrency(Account ac, Currency c, boolean neligible, boolean suspendedForPayOut){
		setAccount(ac);
		setCurrency(c);
		if (neligible) this.setNotEligible(); else this.clearNotEligible();
		if (suspendedForPayOut) setSuspendedForPayOut(); else clearSuspendedForPayOut();
	}
	
	public AccountCurrency(){
		this(null, null, false, false);
	}
	
	// accessors
	
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
	
	public void setNotEligible(){
		this.neligible = true;
	}
	
	public void clearNotEligible(){
		this.neligible = false;
	}
	
	public boolean isNotEligible(){
		return neligible;
	}
	
	public boolean isEligible(){
		return !this.neligible;
	}
	
	public void setSuspendedForPayOut(){
		this.suspendedForPayOut = true;
	}
	
	public void clearSuspendedForPayOut(){
		this.suspendedForPayOut = false;
	}
	
	public boolean isSuspendedForPayOut(){
		return suspendedForPayOut;
	}
	
	
	// JAVA support
	
	public String toString(){
		return "[ac=" + ac.getID() + ", ccy=" + ccy.getID() + ", susPO=" + suspendedForPayOut + "]";
	}
	
	public Object clone(){
		AccountCurrency ac = (AccountCurrency)super.clone();
		return ac;
	}
	
}
