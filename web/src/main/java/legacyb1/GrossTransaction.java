package legacyb1;

import java.util.*;

public class GrossTransaction extends Tr implements Settlable{
	
	// constructors
	
	public GrossTransaction(String ref, String parentRef, Account a1, Currency c1, double am1, Account a2, Currency c2, double am2, String status) throws IllegalAccountException, IllegalCurrencyException{
		super(ref, parentRef, a1, a2, status);
		if (c1 != c2){
			try{
				super.net(new Leg(this, true, c1, am1));
				super.net(new Leg(this, false, c2, am2));
			}catch(TooManyLegsException e){
				e.printStackTrace();
				System.exit(1);
			}
		}else{
			throw new IllegalCurrencyException("Same currencies");
		}
	}
	
	public GrossTransaction(String ref, String parentRef, Account a1, Account a2, String status) throws IllegalAccountException{
		super(ref, parentRef, a1, a2, status);
	}
	
	// miscellaneous
	
	public void net(Leg l) throws TooManyLegsException{
		if (size() >= 2 && getLeg(l.getCurrency()) == null){
			throw new TooManyLegsException("[" + getReference() + "]");
		}else{
			super.net(l);
		}
	}
	
	public String toString(){
		return "Gross transaction: " + super.toString();
	}
	
	public SettlementAlgorithm getAlgorithm(){
		return SettlementAlgorithm.allOrNothing;
	}
	
	public Settlable qualify(SettlementAlgorithm sa){
		Side s1 = getFirstSide();
		Side s2 = getSecondSide();
		System.out.println("RM: Tr=" + getReference() + 
			" [Side=" + getFirstAccount().getName() + " [SPL=" + s1.testSPL() + ", ASPL=" + s1.testASPL() + ", NPOV=" + s1.testNPOV() + "]] " +
			"[Side=" + getSecondAccount().getName() + " [SPL=" + s2.testSPL() + ", ASPL=" + s2.testASPL() + ", NPOV=" + s2.testNPOV() + "]]");
		if (!s1.testSPL() || !s1.testASPL() || !s1.testNPOV()) return null;
		if (!s2.testSPL() || !s2.testASPL() || !s2.testNPOV()) return null;
		return this;
	}
	
	public Tr[] split(){
		double split = 0;
		for(Enumeration e = legs(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			split = Math.max(split, l.getAmount() / l.getCurrency().getGrossSplitThreshold());
		}
		split = Math.floor(split) + 1;
		int size = new Double(split).shortValue();
		// System.out.println("Size=" + size);
		GrossTransaction[] gts = new GrossTransaction[size];
		gts[0] = (GrossTransaction)clone();
		// System.out.println("Original transaction=" + gts[0]);
		if(split > 1){
			GrossTransaction componentgt = (GrossTransaction)clone();
			componentgt.multiply(1 / split);
			componentgt.normalize();
			GrossTransaction subgt = (GrossTransaction)componentgt.clone();
			subgt.inv();
			for(int i = 1; i < split; i++){
				gts[i] = (GrossTransaction)componentgt.clone();
				gts[i].setReference(gts[i].getReference() + "/" + i);
				try{
					gts[0].net(subgt);
				}catch(Throwable e1){
					e1.printStackTrace();
					System.exit(1);
				}
			}
			gts[0].setReference(gts[0].getReference() + "/0");
		}
		return gts;
	}
	
	public Object clone(){
		try{
			GrossTransaction t = new GrossTransaction(getReference(), getParentReference(), getFirstAccount(), getSecondAccount(), getStatus());
			cloneLegs(t);
			return t;
		}catch(IllegalAccountException e2){
			e2.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}