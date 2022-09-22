package legacyb3.core;

public class UMPositionCurrency extends ClsObject {
	private UMPositionWithSM umPos;
	private Currency ccy;
	
	// constructors

	public UMPositionCurrency(UMPositionWithSM umPos, Currency c){
		setUMPos(umPos);
		setCurrency(c);
	}
	
	public UMPositionCurrency(){
		this(null, null);
	}
	
	// accessors
	
	public void setUMPos(UMPositionWithSM umPos){
		this.umPos = umPos;
	}
	
	public UMPositionWithSM getUMPos(){
		return umPos;
	}
	
	public void setCurrency(Currency ccy){
		this.ccy = ccy;
	}
	
	public Currency getCurrency(){
		return ccy;
	}	
	
	
	
	// JAVA support
	
	public String toString(){
		return "[UM Pos= SM : " + umPos.getSM()+ " UM : " + umPos.getUM() + ", ccy=" + ccy.getID() + "]";
	}
	
	public Object clone(){
		UMPositionCurrency umPos = (UMPositionCurrency)super.clone();
		return umPos;
	}
	
}