package legacyb1;

public class Clsb extends DBRecord{
	private TimeOfDay sctt;
	private double mm;
	private double pm;
	private double cm;
	private TimeOfDay opening;
	private TimeOfDay closing;
	private TimeOfDay current;
	
	public void setSCTT(TimeOfDay tod){
		this.sctt = tod;
	}
	
	public TimeOfDay getSCTT(){
		return this.sctt;
	}
	
	public void setMarginMultiplier(double mm){
		this.mm = mm;
	}
	
	public double getMarginMultiplier(){
		return this.mm;
	}
	
	public void setPayMultiplier(double pm){
		this.pm = pm;
	}
	
	public double getPayMultiplier(){
		return this.pm;
	}
	
	public void setCoverMultiplier(double cm){
		this.cm = cm;
	}
	
	public double getCoverMultiplier(){
		return this.cm;
	}
	
	public void setOpening(TimeOfDay tod){
		this.opening = tod;
	}
	
	public TimeOfDay getOpening(){
		return this.opening;
	}
	
	public void setClosing(TimeOfDay tod){
		this.closing = tod;
	}
	
	public TimeOfDay getClosing(){
		return this.closing;
	}
	
	public void setCurrent(TimeOfDay tod){
		this.current = tod;
	}
	
	public TimeOfDay getCurrent(){
		return this.current;
	}
	

	// JAVA support
	
	public Object clone(){
		return new Clsb();
	}
	
	public String toString(){
		return "CLSB: [SCTT=" + sctt + ", mm=" + mm + ", pm=" + pm + ", cm=" + cm + ", opening=" + opening + ", closing=" + closing + "]";
	}
	
}