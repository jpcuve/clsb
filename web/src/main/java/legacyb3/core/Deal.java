package legacyb3.core;

import legacyb3.data.*;

public class Deal extends ClsObject { 
	private Input inp;
	private Currency ccy;
	private boolean sell;
	private double am;
	
	public Deal(Input inp){
		this(inp, null, false, 0);
	}
	
	public Deal(Input inp, Currency ccy, boolean sell, double am){
		this.setInput(inp);
		this.setCurrency(ccy);
		if (sell) this.setSell(); else this.clearSell();
		this.setAmount(am);
	}
	
	public void setInput(Input inp){
		this.inp = inp;
	}
	
	public Input getInput(){
		return this.inp;
	}
	
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public Currency getCurrency(){
		return this.ccy;
	}
	
	public void setSell(){
		this.sell = true;
	}
	
	public void clearSell(){
		this.sell = false;
	}
	
	public boolean isSell(){
		return this.sell;
	}
	
	public void setAmount(double am){
		this.am = am;
	}
	
	public double getAmount(){
		return this.am;
	}
	
	// JAVA support
	
	public String toString(){
		return "[" + (sell ? "BUY  " : "SELL ") + ccy.getID() + " " + Double.toString(am) + "]";
	}
	
	public Object clone(){
		Deal d = (Deal)super.clone();
		return d;
	}
	
}
