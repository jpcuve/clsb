package legacyb1;

public class LinkedCashflowTransaction extends Tr implements Settlable{
	
	public LinkedCashflowTransaction(String ref, String parentRef, Account a1, Account a2, String status) throws IllegalAccountException{
		super(ref, parentRef, a1, a2, status);
	}
	
	public String toString(){
		return "Linked Cashflow transaction: " + super.toString();
	}
	
	public SettlementAlgorithm getAlgorithm(){
		return SettlementAlgorithm.optimalPartial;
	}
	
	public Settlable qualify(SettlementAlgorithm sa){
		System.out.println("Settlement of linked cashflow transactions is not correctly implemented");
		Tr t = (Tr)clone();
		Side s1 = t.getFirstSide();
		Side s2 = t.getSecondSide();
		System.out.println("Side1=\n" + s1 + ", Side2=\n" + s2);
		System.out.println("s1 maxSP=" + s1.maxSP()+ ", s2 maxSP=" + s2.maxSP());
		double sp = Math.min(s1.maxSP(), s2.maxSP());
		if (sp != 1){
			t.multiply(sp);
			s1 = t.getFirstSide();
			s2 = t.getSecondSide();
		}
		System.out.println("Side1=\n" + s1 + ", Side2=\n" + s2);
		System.out.println("s1 maxASP=" + s1.maxASP()+ ", s2 maxASP=" + s2.maxASP());
		double asp = Math.min(s1.maxASP(), s2.maxASP());
		if (asp != 1){
			t.multiply(asp);
			s1 = t.getFirstSide();
			s2 = t.getSecondSide();
		}
		System.out.println("Side1=\n" + s1 + ", Side2=\n" + s2);
		System.out.println("s1 maxNOV=" + s1.maxNOV()+ ", s2 maxNOV=" + s2.maxNOV());
		double nov = Math.min(s1.maxNOV(), s2.maxNOV());
		if (nov != 1){
			t.multiply(nov);
		}
		System.out.println("Original=" + this);
		System.out.println("Settlable=" + t);
		return null;
	}
	
	public Tr[] split(){
		LinkedCashflowTransaction[] ts = new LinkedCashflowTransaction[1];
		ts[0] = (LinkedCashflowTransaction)clone();
		return ts;
	}

	
	// JAVA support
	
	public Object clone(){
		try{
			LinkedCashflowTransaction lct = new LinkedCashflowTransaction(getReference(), getParentReference(), getFirstAccount(), getSecondAccount(), getStatus());
			cloneLegs(lct);
			return lct;
		}catch(IllegalAccountException e2){
			e2.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	


	
}