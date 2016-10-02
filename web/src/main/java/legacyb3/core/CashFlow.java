package legacyb3.core;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.text.*;
import java.util.Iterator;
import java.util.TreeMap;

import legacyb3.or2.TimeOfDay;
import legacyb3.util.*;

public class CashFlow{
	private Hashtable flows;
	
	public CashFlow(){
		this.flows = new Hashtable();
	}
	
	public void addFlow(TimeOfDay tod, double am){
		Double d = (Double)flows.get(tod);
		double val = (d != null) ? d.doubleValue() + am : am;
		flows.put(tod, new Double(val));
	}
	
	public void setFlow(TimeOfDay tod, double am){
		flows.put(tod, new Double(am));
	}
	
	public double getFlow(TimeOfDay tod){
		Double d = (Double)flows.get(tod);
		return (d == null) ? 0 : d.doubleValue();
	}
	
	public double getCumulatedAmount(TimeOfDay tod){
		Iterator i = flows();
		double cumam = 0;
		while(i.hasNext()){
			TimeOfDay t = (TimeOfDay)i.next();
			if(t.isAfter(tod)) break;
			cumam += ((Double)flows.get(t)).doubleValue();
		}
		return cumam;
	}
	
	public double getTotalAmount(){
		double ta = 0;
		for(Enumeration e1 = flows.elements(); e1.hasMoreElements();){
			ta += ((Double)e1.nextElement()).doubleValue();
		}
		return ta;
	}
	
	public int size(){
		return this.flows.size();
	}
	
	public int size(TimeOfDay tod1, TimeOfDay tod2){
		int count = 0;
		Iterator i = this.flows();
		while(i.hasNext()){
			TimeOfDay tod = (TimeOfDay)i.next();
			if(tod.isAfter(tod1)){
				count++;
				break;
			}
		}
		if(count == 1){
			while(i.hasNext()){
				TimeOfDay tod = (TimeOfDay)i.next();
				if(tod.isAfter(tod2)) break;
				count++;
			}
		}
		return count;
	}
	
	public Iterator flows(){
		return this.flows(true);
	}
	
	public Iterator flows(boolean asc){
		TreeMap tm = new TreeMap();
		for(Enumeration e = flows.keys(); e.hasMoreElements();){
			TimeOfDay t = (TimeOfDay)e.nextElement();
			tm.put(new Integer(t.getMinutes()), t);
		}
		return tm.values().iterator();
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		s.append("[\n");
		double cumulated = 0;
		for(Iterator i = this.flows(); i.hasNext();){
			TimeOfDay t = (TimeOfDay)i.next();
			double am = ((Double)flows.get(t)).doubleValue();
			cumulated += am;
			s.append("  [time=" + t + ", cum am=" + cumulated + "]\n");
		}
		s.append("]");
		return s.toString();
	}
	
	public TimeOfDay scan(TimeOfDay tod, boolean asc){
		for(Iterator i = this.flows(asc); i.hasNext();){
			TimeOfDay t = (TimeOfDay)i.next();
			if((asc && t.isAfterOrEqual(tod)) || (!asc  && t.isBeforeOrEqual(tod))) return t;
		}
		return null;
	}
	
	public void adjust(TimeOfDay tod, double am){
		TimeOfDay splitBck = this.scan(tod, false);
		TimeOfDay splitFwd = this.scan(tod, true);
		if(splitBck != null && splitFwd != null && am > 0){
			double d = am;
			this.addFlow(splitBck, d);
			for(Iterator i = this.flows(); i.hasNext() && d > 0.000001;){
				TimeOfDay t = (TimeOfDay)i.next();
				if(t.isAfter(splitBck)){
					double f = this.getFlow(t);
					double delta = Math.min(d, f);
					this.addFlow(t, -delta);
					d -= delta;
				}
			}
		}
	}
	
	public void smooth(Iterator tods){
		Iterator i1 = this.flows();
		TimeOfDay lastTod = new TimeOfDay(0, 0);
		while(tods.hasNext()){
			TimeOfDay tod = (TimeOfDay)tods.next();
			int n = this.size(lastTod, tod);
			System.out.println("  From " + lastTod + " to " + tod + ": range " + n + " points");
			double am = this.getCumulatedAmount(tod) - this.getCumulatedAmount(lastTod);
			for(int i = 0; i < n; i++){
				TimeOfDay t = (TimeOfDay)i1.next();
				setFlow(t, am / n);
			}
			lastTod = tod;
		}
	}
	
	public void ceil(double unit){
		double total = this.getTotalAmount();
		double cumul = 0;
		double cumulCeil = 0;
		for(Iterator i = this.flows(); i.hasNext();){
			TimeOfDay tod = (TimeOfDay)i.next();
			double am = this.getFlow(tod);
			cumul += am;
			double diff = (Math.ceil(cumul / unit - 0.1) * unit) - cumulCeil;
			this.setFlow(tod, diff);
			cumulCeil += diff;
		}
	}
		
}