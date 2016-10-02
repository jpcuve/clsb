package legacyb1;

import java.util.*;

public class Side{
	private Account a;
	private Hashtable flows;
	
	public Side(Account a){
		this.a = a;
		flows = new Hashtable();
	}
	
	public Account getAccount(){
		return this.a;
	}
	
	public double getFlow(Currency ccy){
		Double flow = (Double)flows.get(ccy);
		if (flow == null) return 0;
		return flow.doubleValue();
	}
	
	public double getTotal(String p, boolean volatilityMargin){
		double outFlow = 0;
		double inFlow = 0;
		for(Enumeration e = currencies(); e.hasMoreElements();){
			Currency ccy = (Currency)e.nextElement();
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
		return p.equals("In") ? inFlow : (p.equals("Out") ? outFlow : inFlow - outFlow);
	}
	
	protected Enumeration currencies(){
		return flows.keys();
	}
	
	public boolean testSPL(){
		// System.out.println("SPL");
		for(Enumeration e = currencies(); e.hasMoreElements();){
			Currency ccy = (Currency)e.nextElement();
			// System.out.println(" " + act.getID() + ccy.getName() + " bal=" + a.getBalance(act, ccy) + " flow=" + getFlow(ccy) + " spl=" + a.getSPL(act, ccy));
			if (a.getCurrentBalance(ccy) + getFlow(ccy) < -a.getSPL(ccy)) return false;
		}
		return true;
	}
	
	public boolean testASPL(){
		// System.out.println("ASPL");
		// System.out.println(" short=" + getPosition("Short") + " out=" + s.getTotal("Out") + " aspl=" + getASPL());
		if (a.getPosition("Short", true) + getTotal("Out", true) > a.getASPL()) return false;
		return true;
	}
	
	public boolean testNPOV(){
		// System.out.println("NPOV");
		// System.out.println(" net=" + a.getPosition("Net", true) + " total=" + getTotal("Net", true));
		if (a.getNonCashCollateral() + a.getPosition("Net", true) + getTotal("Net", true) < 0) return false;
		return true;
	}
	
	public double maxSP(){
		double d = 1;
		for(Enumeration e = flows.keys(); e.hasMoreElements();){
			Currency ccy = (Currency)e.nextElement();
			double fl = ((Double)flows.get(ccy)).doubleValue();
			if (fl < 0){
				double fraction = Math.min(-(a.getSPL(ccy) + a.getCurrentBalance(ccy)) / fl, 1);
				d = Math.min(d, fraction);
			}
		}
		return d;
	}
	
	public double maxASP(){
		return Math.min((a.getASPL() - a.getPosition("Short", true)) / getTotal("Out", true), 1);
	}
	
	public double maxNOV(){
		double d = getTotal("Net", true);
		return (d < 0) ? Math.min(-(a.getNonCashCollateral() + a.getPosition("Net", true)) / d, 1) : 1;
	}

	public void net(Currency ccy, double am){
		double d = 0;
		Double flow = (Double)flows.get(ccy);
		if (flow != null) d = flow.doubleValue();
		d += am;
		flows.put(ccy, new Double(d));
	}
	
	public void net(Side s) throws IllegalAccountException{
		if (a != s.getAccount()) throw new IllegalAccountException();
		for(Enumeration e = s.currencies(); e.hasMoreElements();){
			Currency c = (Currency)e.nextElement();
			double fl = s.getFlow(c);
			net(c, fl);
		}
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		for(Enumeration e = currencies(); e.hasMoreElements();){
			Currency ccy = (Currency)e.nextElement();
			s.append("[");
			s.append(a.getName());
			s.append(" ");
			s.append(ccy.getName());
			s.append("=");
			s.append(((Double)flows.get(ccy)).toString());
			s.append("]\n");
		}
		return s.toString();
	}

}