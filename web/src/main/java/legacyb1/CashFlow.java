package legacyb1;

import java.sql.*;
import java.util.*;

public class CashFlow{
	private Hashtable flows;
	
	// private class
	
	public CashFlow(){
		this.flows = new Hashtable();
	}
	
	public void addFlow(TimeOfDay tod, double am){
		Double d = (Double)flows.get(tod);
		flows.put(tod, new Double((d == null) ? am : d.doubleValue() + am));
	}
	
	public double getCumulatedAmount(TimeOfDay tod){
		Enumeration e = flows();
		double cumam = 0;
		while(e.hasMoreElements()){
			TimeOfDay t = (TimeOfDay)e.nextElement();
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
	
	public Enumeration flows(){
		BTree bt = new BTree(new TimeOfDayComparator());
		for(Enumeration e = flows.keys(); e.hasMoreElements();){
			TimeOfDay t = (TimeOfDay)e.nextElement();
			bt.addElement(t);
		}
		return bt.elements();
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		s.append("[\n");
		double cumulated = 0;
		for(Enumeration e = flows(); e.hasMoreElements();){
			TimeOfDay t = (TimeOfDay)e.nextElement();
			double am = ((Double)flows.get(t)).doubleValue();
			cumulated += am;
			s.append("  [time=" + t + ", cum am=" + cumulated + "]\n");
		}
		s.append("]");
		return s.toString();
	}
		
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IllegalAccountException, IllegalCurrencyException{
		CashFlow  now = new CashFlow();
		now.addFlow(new TimeOfDay(10, 35), 1200);
		now.addFlow(new TimeOfDay(10, 40), 2000);
		now.addFlow(new TimeOfDay(10, 50), 300);
		System.out.println(now);
		System.out.println("Cumulated amount at 09:00 AM=" + now.getCumulatedAmount(new TimeOfDay(9, 0)));
		System.out.println("Cumulated amount at 10:42 AM=" + now.getCumulatedAmount(new TimeOfDay(10, 42)));
		System.out.println("Cumulated amount at 10:52 AM=" + now.getCumulatedAmount(new TimeOfDay(10, 52)));
	}
}