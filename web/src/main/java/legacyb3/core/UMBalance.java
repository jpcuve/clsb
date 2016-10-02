package legacyb3.core;

import legacyb3.util.*;
import legacyb3.data.*;

public class UMBalance extends ClsObject { 

	private UMPositionWithSM umPos;
	private SM sm;
	private UM um;
	private BalanceType bt;
	private Currency ccy;
	private double amount;
	
	// constructors

	public UMBalance(UMPositionWithSM umPos, SM sm, UM um,BalanceType bt, Currency c, double am){
		setUMPos(umPos);
		setSM(sm);
		setUM(um);
		setBalanceType(bt);
		setCurrency(c);
		setAmount(am);
	}
	
	public UMBalance(){
		this(null, null,null, null, null, 0);
	}
	
	// accessors
	
	public void setUMPos(UMPositionWithSM umPos){
		this.umPos = umPos;
	}
	
	public UMPositionWithSM getUMPos(){
		return this.umPos;
	}
	
	public void setSM(SM sm){
		this.sm = sm;
	}
	
	public SM getSM(){
		return sm;
	}
	
	public void setUM(UM um){
		this.um = um;
	}
	
	public UM getUM(){
		return um;
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
		setChanged();
	}
	
	public double getAmount(){
		return ccy.round(amount);
	}
	// JAVA support
	
	public String toString(){
		return "[SM=" + getSM().getID() + " , UM=" + getUM().getID() + ", ccy=" + ccy.getID() + ", typ=" + bt.getID()+ ", am=" + amount + "]";
	}
	
	public Object clone(){
		UMBalance b = (UMBalance)super.clone();
		return b;
	}

	// miscellaneous
	
	public double post(double am){
		setAmount(getAmount() + am);
		return amount;
	}
	
}
