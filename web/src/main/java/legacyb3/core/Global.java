package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import legacyb3.data.*;

public class Global extends ClsObject {
	private TimeOfDay now;
	private TimeOfDay sctt;
	private double pom;
	private double pim;
	private TimeOfDay opening;
	private TimeOfDay closing;
	private double minfp;
	private double opsl;
	
	public void setTimeOfDay(TimeOfDay tod){
		this.now = tod;
		this.setChanged();
	}
	
	public TimeOfDay getTimeOfDay(){
		return this.now;
	}	
	
	public void setSCTT(TimeOfDay tod){
		this.sctt = tod;
	}
	
	public TimeOfDay getSCTT(){
		return this.sctt;
	}
	
	public void setPayOutMultiplier(double pom){
		this.pom = pom;
	}
	
	public double getPayOutMultiplier(){
		return this.pom;
	}
	
	public void setPayInMultiplier(double pim){
		this.pim = pim;
	}
	
	public double getPayInMultiplier(){
		return this.pim;
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
	
	public void setMinimumFractionPayable(double minfp){
		this.minfp = minfp;
	}
	
	public double getMinimumFractionPayable(){
		return this.minfp;
	}
	
	public void setOPSLimit(double opsl){
		this.opsl = opsl;
	}
	
	public double getOPSLimit(){
		return this.opsl;
	}

	// JAVA support
	
	public Object clone(){
		Global g = (Global)super.clone();
		return g;
	}
	
	public String toString(){
		return "Global: [SCTT=" + sctt + ", pom=" + pom + ", pim=" + pim + ", opening=" + opening + ", closing=" + closing + "]";
	}
	
}