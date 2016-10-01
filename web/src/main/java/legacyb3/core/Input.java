package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;

import legacyb3.util.*;
import legacyb3.data.*;

public class Input extends ClsObject implements Cloneable {
	private String status;
	private String ref;
	private Party orig;
	private Party cpty;
	private String typ;
	private SM desg;
	private TimeOfDay tod;
	private String match;
	private boolean ManualAuthoriseFlag;
	protected HashMap deals;
	
	// constructors
	
	protected Input(String ref, Party orig, Party cpty, String typ, SM desg, TimeOfDay tod, String st, String match, boolean manAuth){
		this.setStatus(st);
		this.setReference(ref);
		this.setOriginator(orig);
		this.setCounterParty(cpty);
		this.setType(typ);
		this.setDesignatedSM(desg);
		this.setTimeOfDay(tod);
		this.setStatus(st);
		this.setMatch(match);
		if(manAuth) this.setManualAuthoriseFlag(); else this.clearManualAuthoriseFlag();
		deals = new HashMap();
	}
	
	public Input(String ref){
		this(ref, null, null, "LC", null, null, null, null, false);
	}
	
	// accessors
	
	public void setOriginator(Party orig){
		this.orig = orig;
	}
	
	public Party getOriginator(){
		return this.orig;
	}
	
	public void setCounterParty(Party cpty){
		this.cpty = cpty;
	}
		
	public Party getCounterParty(){
		return this.cpty;
	}
	
	public void setType(String typ){
		this.typ = typ;
	}

	public String getType(){
		return this.typ;
	}
	
	public boolean isLCF(){
		return this.typ.equals("LC");
	}
	
	public void setDesignatedSM(SM desg){
		this.desg = desg;
	}
	
	public SM getDesignatedSM(){
		return this.desg;
	}
	
	public void setStatus(String status){
		this.status = status;
		this.setChanged();
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setReference(String ref){
		this.ref = ref;
	}
	
	public String getReference(){
		return this.ref;
	}
	
	public void setTimeOfDay(TimeOfDay tod){
		this.tod = tod;
	}
	
	public TimeOfDay getTimeOfDay(){
		return this.tod;
	}
	
	public void setMatch(String match){
		this.match = match;
	}
	
	public String getMatch(){
		return this.match;
	}
	
	public boolean isManualAuthoriseFlag(){
		return this.ManualAuthoriseFlag;		
	}
	
	public void setManualAuthoriseFlag(){
		this.ManualAuthoriseFlag = true;
		this.setChanged();
	}

	public void clearManualAuthoriseFlag(){
		this.ManualAuthoriseFlag = false;
		this.setChanged();
	}

	public void setDeal(Deal d){
		deals.put(d.getCurrency(), d);
	}

	public Deal getDeal(Currency c){
		return (Deal)deals.get(c);
	}
	
	public int size(){
		return deals.size();
	}
	
	public Iterator deals(){
		return deals.values().iterator();
	}
	
	// miscellaneous
	
	public String getMaleKey(){
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.getOriginator().getID());
		sb.append(this.getCounterParty().getID());
		sb.append(this.getType());
		sb.append(this.size());
		return sb.toString();
	}
	
	public String getFemaleKey(){
		StringBuffer sb = new StringBuffer(128);
		sb.append(this.getCounterParty().getID());
		sb.append(this.getOriginator().getID());
		sb.append(this.getType());
		sb.append(this.size());
		return sb.toString();
	}
	
	public boolean match(Input inp){
		/*
		if(this.isLCF() ^ inp.isLCF()) return false;
		if(!this.getOriginator().equals(inp.getCounterParty())) return false;
		if(!this.getCounterParty().equals(inp.getOriginator())) return false;
		if(this.size() != inp.size()) return false;
		*/
		for(Iterator e = this.deals(); e.hasNext();){
			Deal d = (Deal)e.next();
			Deal d2 = inp.getDeal(d.getCurrency());
			if (d2 == null) return false;
			else{
				if(d.isSell() ==  d2.isSell()) return false;
				Currency c = d.getCurrency();
				int prec = c.getPrecision();
				if(c.round(Math.abs(d.getAmount() - d2.getAmount())) > Math.pow(10, prec)) return false;
			}
		}
		return true;
	}
	
	public double getApproximateSettlementAmount(){
		double settlAmountSell = 0;
		double settlAmountBuy = 0;
		for(Iterator e = this.deals(); e.hasNext();){
			Deal d = (Deal)e.next();
			Currency c = d.getCurrency();
			double am = d.getAmount();
			// if dir is true, a1 is debited
			if (d.isSell()){
				settlAmountSell += am * c.getRate();
			}else{
				settlAmountBuy += am * c.getRate();
			}
		}
		return (settlAmountSell + settlAmountBuy) / 2;
	}

		
	// JAVA support
	
	public String toString(){
		return "[Ref=" + ref + ", orig=" + orig.getID() + ", cpty=" + cpty.getID() + ", desg=" + desg.getID() + ", St=" + status + ", due=" + tod + "]";
	}
	
	public String toStringFull(){
		StringBuffer s = new StringBuffer(1024);
		s.append(toString());
		for(Iterator e = this.deals(); e.hasNext();){
			s.append("\n  ");
			s.append(((Deal)e.next()).toString());
		}
		return s.toString();
	}
	
	public Object clone(){
		Input i = (Input)super.clone();
		i.deals = (HashMap)this.deals.clone();
		return i;
	}
	
	public Object cloneDeep() throws SQLException {
		Input i = (Input)this.clone();
		for(Iterator e1 = this.deals(); e1.hasNext();){
			Deal d = (Deal)e1.next();
			i.setDeal((Deal)d.clone());
		}
		return i;
	}

}