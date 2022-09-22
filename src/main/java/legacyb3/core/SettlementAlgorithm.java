package legacyb3.core;

import legacyb3.data.Currencies;
import legacyb3.data.Globals;

import java.sql.SQLException;

public class SettlementAlgorithm{
	
	private Currencies ct;
	private Globals gt; // added by pha
	
	public static SettlementAlgorithm allOrNothing = null;;
	public static SettlementAlgorithm optimalPartial = null;
	
	public static void instance(Globals gt, Currencies ct){
		if (allOrNothing == null){
			SettlementAlgorithm.allOrNothing = new SettlementAlgorithm(gt, ct);
			SettlementAlgorithm.optimalPartial = new SettlementAlgorithm(gt, ct);
		}
	}

	protected SettlementAlgorithm(Globals gt, Currencies ct){
		this.ct = ct;
		this.gt = gt; // added by pha
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
	
	public double getMinimumFractionPayable(){//added by pha
		double MinFP = gt.get().getMinimumFractionPayable();
		return MinFP; 
	}
	
	public double getOPSLimit(){//added by pha
		double MinFP = gt.get().getOPSLimit();
		return MinFP; 
	}
}