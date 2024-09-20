package legacyb3.core;

import legacyb3.data.Currencies;
import legacyb3.data.Globals;
import legacyb3.or2.TimeOfDay;

import java.sql.SQLException;
import java.util.Iterator;

public class PayOutAlgorithm { 

	private Currencies ct;
	private Globals gt;
	
	public static PayOutAlgorithm po = null;
	
	public static PayOutAlgorithm instance(Globals gt, Currencies ct){
		if (po == null){
			PayOutAlgorithm po = new PayOutAlgorithm(gt, ct);
			PayOutAlgorithm.po = po;
		}
		return po;
	}
	
	protected PayOutAlgorithm(Globals gt, Currencies ct){
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
	
	public Iterator fctts(boolean dir)throws SQLException{
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
	
	public double getPayOutMultiplier(){
		return gt.get().getPayOutMultiplier();
	}
		
}

