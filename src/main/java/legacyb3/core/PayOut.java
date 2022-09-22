package legacyb3.core;

import legacyb3.or2.TimeOfDay;

import java.util.Enumeration;
import java.util.Vector;

public class PayOut extends ClsObject implements Bookable {
	
	private String ref;
	private Account ac;
	private Currency ccy;
	private String status;
	private TimeOfDay tod;
	private double am;
	
	public PayOut(){
		this(null, null, null, null, null, 0);
	}
	
	public PayOut(String ref, Account ac, Currency ccy, String status, TimeOfDay tod, double am){
		setReference(ref);
		setAccount(ac);
		setCurrency(ccy);
		setStatus(status);
		setTimeOfDay(tod);
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
	
	// JAVA support
	
	public String toString(){
		return "[Reference=" + ref + ", ac=" + ac + ", ccy=" + ccy + ", st=" + status + ", tod=" + tod +", am=" + am + "]";
	}
	
	public Object clone(){
		PayOut po = (PayOut)super.clone();
		return po;
	}
	
	// bookable
	
	public Enumeration movements(){
		Vector v = new Vector();
		v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.po , ac, ccy.getMirrorAccount(), ccy, am));
		v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.ou, ccy.getMirrorAccount(), ac, ccy, am));
		return v.elements();
	}
			
	// miscellaneous

	public PayOut[] split(){
		double max = ccy.floorUnit(ccy.getMaximumPayOut());
		double split = am / max;
		split = Math.ceil(split);
		int size = new Double(split).shortValue();
		// System.out.println("Size=" + size);
		PayOut[] ps = new PayOut[size];
		ps[0] = (PayOut)clone();
		// System.out.println("Original payout=" + ps[0]);
		if(split > 1){
			PayOut componentp = (PayOut)clone();
			double d = ccy.ceilUnit(componentp.getAmount() / split);
			componentp.setAmount(d);
			for(int i = 1; i < split; i++){
				ps[i] = (PayOut)componentp.clone();
				ps[i].setReference(ps[i].getReference() + "/" + i);
			}
			double lastAmount = ps[0].getAmount() - (size - 1) * d;
			lastAmount = ccy.round(lastAmount);
			ps[0].setAmount(lastAmount);
			ps[0].setReference(ps[0].getReference() + "/0");
		}else{
			ps[0].setAmount(ccy.round(ps[0].getAmount()));
		}
		return ps;
	}

}