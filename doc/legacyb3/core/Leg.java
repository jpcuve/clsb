package legacyb3.core;

public class Leg extends ClsObject {
	private Transaction tr;
	private boolean dir;
	private Currency ccy;
	private double am;
	
	// constructor
	
	public Leg(Transaction tr, boolean dir, Currency ccy, double am){
		this.setTransaction(tr);
		if(dir) this.setDir(); else this.clearDir();
		this.setCurrency(ccy);
		this.setAmount(am);
	}
	
	// accessors
	
	public Transaction getTransaction(){
		return tr;
	}
	
	public void setTransaction(Transaction tr){
		this.tr = tr;
	}
	
	public void setDir(){
		this.dir = true;
	}
	
	public void clearDir(){
		this.dir = false;
	}
	
	public boolean isDir(){
		return this.dir;
	}
	
	public Currency getCurrency(){
		return ccy;
	}
	
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public double getAmount(){
		return am;
	}
	
	public void setAmount(double am){
		this.am = am;
	}
	
	// JAVA support
	
	public String toString(){
		return "[" + (dir ? "-> " : "<- ") + ccy.getID() + " " + Double.toString(am) + "]";
	}
	
	public Object clone(){
		Leg l = (Leg)super.clone();
		return l;
	}
	
	// miscellaneous
	
	public void normalize(){
		if (am < 0){
			dir = !dir;
			am = -am;
		}
		am = ccy.round(am);
	}
	
	public void inv(){
		dir = !dir;
	}
	
	public void multiply(double m){
		this.am *= m;
	}
	
	public void divide(double m){
		this.am /= m;
	}
	
	public void addAmount(boolean b, double d){
		if(b ^ this.dir) this.am -= d;
		else this.am += d;
		this.normalize();
	}
	
	/*
	public void netLeg(Leg l) throws IllegalCurrencyException{
		if (l.getCurrency() != ccy){
			throw new IllegalCurrencyException();
		}
		if (l.getDir() == dir){
				am += l.getAmount();
		} else {
				am -= l.getAmount();
				normalize();
		}
	}
	*/
}