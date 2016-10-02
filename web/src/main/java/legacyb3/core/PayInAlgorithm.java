package legacyb3.core;

import java.util.Enumeration;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeMap;

import legacyb3.or2.TimeOfDay;
import legacyb3.data.*;
import legacyb3.util.*;

public class PayInAlgorithm { 

	private Currencies ct;
	private Globals gt;
	
	public static PayInAlgorithm pi = null;
	
	public static PayInAlgorithm instance(Globals gt, Currencies ct){
		if (pi == null){
			PayInAlgorithm pi = new PayInAlgorithm(gt, ct);
			PayInAlgorithm.pi = pi;
		}
		return pi;
	}
	
	protected PayInAlgorithm(Globals gt, Currencies ct){
		this.gt = gt;
		this.ct = ct;
	}
	
	public CurrencyPriorityIterator currenciesPerLiquidity(boolean dir){
		CurrencyPriorityIterator e = null;
		try{
			e = (CurrencyPriorityIterator)ct.currenciesPerLiquidity(dir);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return e;
	}
	
	public Iterator fctts(boolean dir){
		FcttIterator i = null;
		try{
			i = (FcttIterator)ct.fctts(dir);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return i;
	}
	
	public TimeOfDay getSCTT(){
		return gt.get().getSCTT();
	}
	
	public double getPayInMultiplier(){
		return gt.get().getPayInMultiplier();
	}
	
	public Iterator primaryPoints(Currency ccy){
		TreeMap pps = new TreeMap();
		TimeOfDay sctt = gt.get().getSCTT();
		pps.put(new Integer(sctt.getMinutes()), sctt);
		TimeOfDay fctt = ccy.getFCTT();
		pps.put(new Integer(fctt.getMinutes()), fctt);
		Iterator i = null;
		i = ccy.payInTimes(true);
		if(i.hasNext()){
			TimeOfDay t = (TimeOfDay)i.next();
			pps.put(new Integer(t.getMinutes()), t);
		}
		i = ccy.payInTimes(false);
		if(i.hasNext()){
			TimeOfDay t = (TimeOfDay)i.next();
			pps.put(new Integer(t.getMinutes()), t);
		}
		return pps.values().iterator();
	}
	
}

