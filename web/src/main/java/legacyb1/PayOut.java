package legacyb1;

public class PayOut extends DBRecord{
	
	private String ref;
	private Account ac;
	private Currency ccy;
	private double am;
	
	public PayOut(){
		this(null, null, null, 0);
	}
	
	public PayOut(String ref, Account ac, Currency ccy, double am){
		setReference(ref);
		setAccount(ac);
		setCurrency(ccy);
		setAmount(am);
	}
	
	public void setReference(String ref){
		this.ref = ref;
	}
	
	public String getReference(){
		return this.ref;
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
	
	public void setAmount(double am){
		this.am = am;
	}
	
	public double getAmount(){
		return this.am;
	}
	
	// JAVA support
	
	public String toString(){
		return "[Reference=" + ref + ", ac=" + ac + ", ccy=" + ccy + ", am=" + am + "]";
	}
	
	public Object clone(){
		return new PayOut(ref, ac, ccy, am);
	}
	
	// miscellaneous

	public PayOut[] split(){
		double split = am / ccy.getMaximumPayOut();
		split = Math.floor(split) + 1;
		int size = new Double(split).shortValue();
		// System.out.println("Size=" + size);
		PayOut[] ps = new PayOut[size];
		ps[0] = (PayOut)clone();
		// System.out.println("Original payout=" + ps[0]);
		if(split > 1){
			PayOut componentp = (PayOut)clone();
			double d = ccy.round(componentp.getAmount() / split);
			componentp.setAmount(d);
			for(int i = 1; i < split; i++){
				ps[i] = (PayOut)componentp.clone();
				ps[i].setReference(ps[i].getReference() + "/" + i);
			}
			ps[0].setAmount(ps[0].getAmount() - (size - 1) * d);
			ps[0].setReference(ps[0].getReference() + "/0");
		}
		return ps;
	}

}