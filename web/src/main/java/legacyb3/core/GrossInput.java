package legacyb3.core;

import java.sql.*;
import java.util.Iterator;
import java.util.Vector;

import legacyb3.or2.TimeOfDay;
import legacyb3.util.*;
import legacyb3.data.*;

public class GrossInput extends Input {
	private Currency buyCcy;
	private double buyAm;
	private Currency sellCcy;
	private double sellAm;
	
	// constructors
	
	protected GrossInput(String ref, Party orig, Party cpty, SM desg, TimeOfDay tod, String st, String match,  boolean manAuth, Currency buyCcy, double buyAm, Currency sellCcy, double sellAm){
		super(ref, orig, cpty, TransactionType.GROSS , desg, tod, st, match,manAuth);
		this.setBuyCurrency(buyCcy);
		this.setBuyAmount(buyAm);
		this.setSellCurrency(sellCcy);
		this.setBuyAmount(sellAm);
	}
	
	public GrossInput(String ref){
		this(ref, null, null, null, null, null, null, false, null, 0, null, 0);
	}
	
	// accessors
	
	public void setBuyCurrency(Currency ccy){
		this.buyCcy = ccy;
	}
	
	public Currency getBuyCurrency(){
		return this.buyCcy;
	}
	
	public void setBuyAmount(double am){
		this.buyAm = am;
	}
	
	public double getBuyAmount(){
		return this.buyAm;
	}
	
	public void setSellCurrency(Currency ccy){
		this.sellCcy = ccy;
	}
	
	public Currency getSellCurrency(){
		return this.sellCcy;
	}
	
	public void setSellAmount(double am){
		this.sellAm = am;
	}
	
	public double getSellAmount(){
		return this.sellAm;
	}
	
	public void setDeal(Deal d){
		Currency ccy = d.getCurrency();
		double am = d.getAmount();
		if(d.isSell()){
			this.setSellCurrency(ccy);
			this.setSellAmount(am);
		}else{
			this.setBuyCurrency(ccy);
			this.setBuyAmount(am);
		}
	}

	public Deal getDeal(Currency c){
		if(c.equals(this.getBuyCurrency())) return new Deal(this, this.getBuyCurrency(), false, this.getBuyAmount());
		if(c.equals(this.getSellCurrency())) return new Deal(this, this.getSellCurrency(), true, this.getSellAmount());
		return null;
	}
	
	public int size(){
		return 2;
	}
	
	public Iterator deals(){
		Vector v = new Vector();
		v.addElement(new Deal(this, this.getBuyCurrency(), false, this.getBuyAmount()));
		v.addElement(new Deal(this, this.getSellCurrency(), true, this.getSellAmount()));
		return v.iterator();
	}
	
	// miscellaneous
	
	public String getMaleKey(){
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.getOriginator().getID());
		sb.append(this.getCounterParty().getID());
		sb.append(this.getType());
		sb.append(this.size());
		sb.append(this.getSellCurrency().getID());
		sb.append(this.getBuyCurrency().getID());
		return sb.toString();
	}
	
	public String getFemaleKey(){
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.getCounterParty().getID());
		sb.append(this.getOriginator().getID());
		sb.append(this.getType());
		sb.append(this.size());
		sb.append(this.getBuyCurrency().getID());
		sb.append(this.getSellCurrency().getID());
		return sb.toString();
	}
	
	public boolean match(Input inp){
		/*
		if(this.isLCF() ^ inp.isLCF()) return false;
		if(!this.getOriginator().equals(inp.getCounterParty())) return false;
		if(!this.getCounterParty().equals(inp.getOriginator())) return false;
		GrossInput ginp = (GrossInput)inp;
		if(!this.getBuyCurrency().equals(ginp.getSellCurrency())) return false;
		if(!this.getSellCurrency().equals(ginp.getBuyCurrency())) return false;
		*/
		GrossInput ginp = (GrossInput)inp;
		Currency c = this.getSellCurrency();
		int prec = c.getPrecision();
		if(c.round(Math.abs(this.getSellAmount() - ginp.getBuyAmount())) > Math.pow(10, prec)) return false;
		c = this.getBuyCurrency();
		prec = c.getPrecision();
		if(c.round(Math.abs(this.getBuyAmount() - ginp.getSellAmount())) > Math.pow(10, prec)) return false;
		return true;
	}
	
	public double getApproximateSettlementAmount(){
		return (this.getBuyAmount() * this.getBuyCurrency().getRate() + this.getSellAmount() * this.getSellCurrency().getRate()) / 2;
	}

		
	// JAVA support
		
	public Object clone(){
		GrossInput gi = (GrossInput)super.clone();
		return gi;
	}
	
	public Object cloneDeep() throws SQLException {
		return this.clone();
	}
	
	
}
