package legacyb1;

public class Leg extends DBRecord {
	private Tr tr;
	private boolean dir;
	private Currency ccy;
	private double am;
	
	// constructor
	
	public Leg(Tr tr, boolean dir, Currency ccy, double am){
		this.tr = tr;
		this.dir = dir;
		this.ccy = ccy;
		this.am = am;
	}
	
	// accessors
	
	public Tr getTr(){
		return tr;
	}
	
	public void setTr(Tr tr){
		this.tr = tr;
	}
	
	public boolean getDir(){
		return dir;
	}
	
	public void setDir(boolean dir){
		this.dir = dir;
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
		return "[" + (dir ? "-> " : "<- ") + ccy.getName() + " " + Double.toString(am) + "]";
	}
	
	public Object clone(){
		return new Leg(tr, dir, ccy, am);
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
		am *= m;
	}
	
	public void net(Leg l) throws IllegalCurrencyException {
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
	
}