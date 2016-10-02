package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import java.util.*;
import legacyb3.util.*;

public class Side implements Cloneable{
	protected HashMap flows;
	
	public static int IN = 0;
	public static int OUT = 1;
	public static int NET = 2;
	
	public Side(){
		flows = new HashMap();
	}
	
	public double getFlow(Currency ccy){
		Double flow = (Double)flows.get(ccy);
		if (flow == null) return 0;
		return flow.doubleValue();
	}

	public void setFlow(Currency ccy, double am){
		double d = ccy.round(am);
		flows.put(ccy, new Double(d));
	}
	
	public void netFlow(Currency ccy, double am){
		double d = 0;
		Double flow = (Double)flows.get(ccy);
		if (flow != null) d = flow.doubleValue();
		d = ccy.round(d + am);
		flows.put(ccy, new Double(d));
	}
	
	public void copyFlows(Side s, double mult, boolean debit){// created by pha
		// notes by pha : mult = -1 of 1
		for(Iterator i = s.currencies(); i.hasNext();){
			Currency c = (Currency)i.next();
			double fl = s.getFlow(c);
			if (fl != 0){
				if (((fl < 0) && (debit)) || ((fl > 0) && (!debit)))  {
			        this.setFlow(c, mult * fl);	}
			}
		}	
	}	
	
	public void cleanFlows(boolean debit){// created by pha
		// this check another side if has no a ccy this ccy is removed from the object
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency c = (Currency)i.next();
			double fl = this.getFlow(c);
			if((debit && fl < 0) || (!debit && fl > 0)){
				// flows.remove(c);
				i.remove(); // changed by JPC
			}
		}		
	}

	public void net(Side s){
		for(Iterator i = s.currencies(); i.hasNext();){
			Currency c = (Currency)i.next();
			double fl = s.getFlow(c);
			netFlow(c, fl);
		}
	}

	public double getTotal(int type, boolean volatilityMargin){
		double outFlow = 0;
		double inFlow = 0;
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			double fl = ((Double)flows.get(ccy)).doubleValue();
			double vm = ccy.getVolatilityMargin();
			double factor = volatilityMargin ? ((fl < 0) ? (1 + vm) : (1 - vm)) : 1;
			double rate = ccy.getRate() * factor;
			if (fl > 0){
				inFlow += fl * rate;
			}else{
				outFlow -= fl * rate;
			}
		}
		if(type == Side.IN) return inFlow;
		if(type == Side.OUT) return outFlow;
		return (inFlow - outFlow);
	}
	
	public double getTotal(int type, boolean volatilityMargin, TimeOfDay tod){
		
		if (tod == null) {return getTotal(type, volatilityMargin);}
		
		double outFlow = 0;
		double inFlow = 0;
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			// TODO: PHA check usage of Net Overall Value
			// if (ccy.getCC().isAfter(tod)) {
			double fl = ((Double)flows.get(ccy)).doubleValue();
			if(!(fl > 0 && tod.isAfter(ccy.getRTGSClose()))){
				double vm = ccy.getVolatilityMargin();
				double factor = volatilityMargin ? ((fl < 0) ? (1 + vm) : (1 - vm)) : 1;
				double rate = ccy.getRate() * factor;
				if (fl > 0){
				inFlow += fl * rate;
				}else{
					outFlow -= fl * rate;
				}
			}
		}
		if(type == Side.IN ) return inFlow;
		if(type == Side.OUT ) return outFlow;
		return (inFlow - outFlow);
	}
	
	public Iterator currencies(){
		return flows.keySet().iterator();
	}
	public boolean testSPL(Account a){
		// System.out.println("SPL");
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			// System.out.println(" " + act.getID() + ccy.getName() + " bal=" + a.getBalance(act, ccy) + " flow=" + getFlow(ccy) + " spl=" + a.getSPL(act, ccy));
			if (a.getCurrentBalance(ccy) + getFlow(ccy) < -a.getSPL(ccy)) return false;
		}
		return true;
	}
	
	public boolean testASPL(Account a){
		// System.out.println("Transaction : " + this);
		Side s = a.getSide();
		// System.out.println("Account : " + s);
		s.net(this);
		// System.out.println("Projected : " + s);
		// System.out.println("Short aggregate : " + s.getTotal("Out", true));
		// System.out.println("ASPL : " + a.getASPL());
		if (s.getTotal(Side.OUT, true) > a.getASPL()) return false;
		return true;
	}
	
	public boolean testNPOV(Account a, TimeOfDay tod)  {
		// System.out.println("NPOV");
		// System.out.println(" net=" + a.getPosition("Net", true) + " total=" + getTotal("Net", true));
		Side s = a.getSide();
		s.net(this);
		if (a.getNonCashCollateral() + s.getTotal(Side.NET, true, tod) < 0) return false;
		return true;
	}
	
	public void excessSPL(Account a){
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			double spl = a.getSPL(ccy);
			double dif = getFlow(ccy) + spl;
			if(dif < 0) setFlow(ccy, -dif);
			else setFlow(ccy, 0);
		}
	}
	
	public double testASPLpure(Account a)  {	// added by pha			
		double test = getTotal(Side.OUT , true);
		return (test - a.getASPL());
	}
	
	public double testNPOVpure(Account a, TimeOfDay tod)  { // added by pha
		double test = getTotal(Side.NET , true, tod);
		return a.getNonCashCollateral() + test;
	}

	
	public void basket(double reducedAmount, CurrencyPriorityIterator e){
		while(e.hasMoreElements() && reducedAmount > 0){
			Currency ccy = (Currency)e.nextElement();
			double am = getFlow(ccy);
			if(am < 0){
				double rate = ccy.getRate();
				if(reducedAmount > -am * rate){
					setFlow(ccy, 0);
					reducedAmount += am * rate;
				}else{
					netFlow(ccy, reducedAmount / rate);
					reducedAmount = 0;
				}
			}
		}
	}
	
	// java support
	
	public Object clone(){
		Side s = null;
		try{
			s = (Side)super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
		}
		s.flows = (HashMap)this.flows.clone();
		return s;
	}
	
	public Object cloneDeep(){
		Side s = (Side)this.clone();
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			s.setFlow(ccy, this.getFlow(ccy));
		}
		return s;
	}
		
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			Double dl = (Double)flows.get(ccy);
			double d = (dl == null) ? 0 : dl.doubleValue();
			s.append("[" + ccy.getID() + "=" + d + " (" + d * ccy.getRate() + ")]\n");
		}
		return s.toString();
	}

}