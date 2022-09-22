package legacyb3.core;

import legacyb3.or2.TimeOfDay;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Transaction extends ClsObject implements Bookable, Settlable {
	private String status;
	private String ref;
	private String parentRef;
	private Account a1;
	private Account a2;
	private String typ;
	protected HashMap ls;
	
	// constructors
	
	protected Transaction(String ref, String parentRef, Account a1, Account a2, String typ, String st){
		this.setStatus(st);
		this.setReference(ref);
		this.setParentReference(parentRef);
		this.setFirstAccount(a1);
		this.setSecondAccount(a2);
		this.setType(typ);
		ls = new HashMap();
	}
	
	protected Transaction(String ref, String parentRef, Account a1, Side s1, Account a2, Side s2, String typ, int type, String st){
		this(ref, parentRef, a1, a2, typ, st);
		for(Iterator i = s1.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			double am = s1.getFlow(ccy);
			if(type == Side.OUT  && am < 0) this.addLeg(new Leg(this, true, ccy, -am));
			else if(type == Side.IN  && am > 0) this.addLeg(new Leg(this, false, ccy, am));
		}
		for(Iterator i = s2.currencies(); i.hasNext();){
			Currency ccy = (Currency)i.next();
			double am = s2.getFlow(ccy);
			if(type == Side.OUT  && am < 0) this.addLeg(new Leg(this, false, ccy, -am));
			else if(type == Side.IN  && am > 0) this.addLeg(new Leg(this, true, ccy, am));
		}
	}
	
	public Transaction(String ref){
		this(ref, "_top_", null, null, "GT", null);
	}
	
	// accessors
	
	public void setAccount(Account a){
		this.setFirstAccount(a);
	}
	
	public Account getAccount(){
		return this.getFirstAccount();
	}
	
	public void setFirstAccount(Account a){
		this.a1 = a;
	}
		
	public Account getFirstAccount(){
		return a1;
	}
	
	public void setOtherAccount(Account a){
		this.setSecondAccount(a);
	}
	
	public Account getOtherAccount(){
		return this.getSecondAccount();
	}
	
	public void setSecondAccount(Account a){
		this.a2 = a;
	}
		
	public Account getSecondAccount(){
		return a2;
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
	
	public void setStatus(String status){
		this.status = status;
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
	
	public void setParentReference(String parentRef){
		this.parentRef = parentRef;
	}
	
	public String getParentReference(){
		return this.parentRef;
	}
	
	public void setLeg(Leg l){
		Currency ccy = l.getCurrency();
		ls.put(ccy, l);
	}
			
	public Leg getLeg(Currency c){
		return (Leg)ls.get(c);
	}

	public int size(){
		return ls.size();
	}
	
	public Iterator legs(){
		return ls.values().iterator();
	}
	
	public Enumeration movements(MovementType mt, boolean direction){
		Vector v = new Vector();
		for(Iterator e = this.legs(); e.hasNext();){
			Leg l = (Leg)e.next();
			boolean dir = l.isDir();
			Movement m = new Movement(Movement.makeReference("MV"), ref, mt, (dir ^ direction) ? a2 : a1, (dir ^ direction) ? a1 : a2, l.getCurrency(), l.getAmount());
			v.addElement(m);
		}
		return v.elements();
	}
	
	// Bookable
	
	public Enumeration movements(){
		Vector v = new Vector();
		for(Iterator e = this.legs(); e.hasNext();){
			Leg l = (Leg)e.next();
			boolean dir = l.isDir();
			v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.se, (dir) ? a1 : a2, (dir) ? a2 : a1, l.getCurrency(), l.getAmount()));
		}		
		return v.elements();
	}
	
	public Enumeration prMovements(boolean direction){
		Vector v = new Vector();
		for(Iterator e = this.legs(); e.hasNext();){
			Leg l = (Leg)e.next();
			boolean dir = l.isDir();
			v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.pr, (dir ^ direction) ? a2 : a1, (dir ^ direction) ? a1 : a2, l.getCurrency(), l.getAmount()));
		}		
		return v.elements();
	}
		
	// JAVA support
	
	public String toString(){
		return "[Ref=" + ref + ", parent=" + parentRef + ", Ac=" + a1.getID() + " " + a2.getID() + ", St=" + status + ", Sa(+/-)=" + getApproximateSettlementAmount() + "]";
	}
	
	public String toStringFull(){
		StringBuffer s = new StringBuffer(1024);
		s.append(toString());
		for(Iterator e = this.legs(); e.hasNext();){
			s.append("\n  ");
			s.append(((Leg)e.next()).toString());
		}
		return s.toString();
	}
	
	public Object clone(){
		Transaction t = (Transaction)super.clone();
		t.ls = (HashMap)this.ls.clone();
		return t;
	}
	
	public Object cloneDeep() throws SQLException {
		Transaction t = (Transaction)this.clone();
		t.ls = new HashMap();
		for(Iterator e1 = this.legs(); e1.hasNext();){
			Leg l = (Leg)e1.next();
			Leg lClone = (Leg)l.clone();
			lClone.setTransaction(t);
			t.setLeg(lClone);
		}
		return t;
	}
	
	// miscellaneous
	
	public Transaction[] split() throws SQLException{
		Transaction[] ts = new Transaction[1];
		ts[0] = (Transaction)this.cloneDeep();
		return ts;
	}
	
	public SettlementAlgorithm getSettlementAlgorithm(boolean failureManagement){
		return SettlementAlgorithm.optimalPartial;
	}
	
	public Settlable qualify(SettlementAlgorithm sa, TimeOfDay tod) {
		if(sa == SettlementAlgorithm.allOrNothing) return this.qualifyAllOrNothing(sa, tod);
		if(sa == SettlementAlgorithm.optimalPartial) return this.qualifyOptimalPartial(sa, tod);
		return null;
	}
	
	private Settlable qualifyAllOrNothing(SettlementAlgorithm sa, TimeOfDay tod) {
		Side s1 = this.getFirstSide();
		Side s2 = this.getSecondSide();
		System.out.println("RM: Tr=" + getReference() + 
			" [Side=" + a1.getID() + " [SPL=" + s1.testSPL(a1) + ", ASPL=" + s1.testASPL(a1) + ", NPOV=" + s1.testNPOV(a1, tod) + "]] " +
			"[Side=" + a2.getID() + " [SPL=" + s2.testSPL(a2) + ", ASPL=" + s2.testASPL(a2) + ", NPOV=" + s2.testNPOV(a2, tod) + "]]");
		if (!s1.testSPL(a1) || !s1.testASPL(a1) || !s1.testNPOV(a1, tod)) return null;
		if (!s2.testSPL(a2) || !s2.testASPL(a2) || !s2.testNPOV(a2, tod)) return null;
		return this;
	}
		
	private Settlable qualifyOptimalPartial(SettlementAlgorithm sa, TimeOfDay tod) {
		Side[] t = new Side[2];
		Side[] s = new Side[2];
		Account[] a = new Account[2];
		Side[] origS = new Side[2];
		double[] tot = new double[2];
		double[] frac = new double[2];
		// SPL test
		for(int i = 0; i < 2; i++){
			t[i] = (i == 0) ? this.getFirstSide() : this.getSecondSide();
			tot[i] = t[i].getTotal(Side.OUT , false);
			a[i] = (i == 0) ? this.getFirstAccount() : this.getSecondAccount();
			s[i] = a[i].getSide();
			// calculate SPL excess
			origS[i] = (Side) s[i].cloneDeep();
			s[i].net(t[i]);
			s[i].excessSPL(a[i]);
			// calculate max amounts payable
			t[i].net(s[i]);
			// calculate fraction
			frac[i] = t[i].getTotal(Side.OUT , false) / tot[i];
		}
		int k = (frac[0] < frac[1]) ? 0 : 1;
		int l = (k + 1) % 2;
		double minFp = sa.getMinimumFractionPayable();
		// if common fraction < minimum fraction payable => legs = 0 %
		if (frac[k] < minFp) return null;
		// Basket of value with reduced amounts
		t[l].basket((frac[l] - frac[k]) * tot[l], sa.currenciesPerLiquidity(false));
	
		for(int i = 0; i < 2; i++){
			t[(i + 1) % 2].cleanFlows(false);	
			t[(i+1) % 2].copyFlows(t[i], -1, true); 
			s[(i+1) % 2] = (Side) origS[(i+1) % 2].cloneDeep();
			s[(i+1) % 2].net(t[(i+1) % 2]);
			}

		// first test for nov and asple if not succeed => launch binary search
		double[] asple_nov = new double[4];
		boolean continuesearch = false;
		boolean asple_nov_test = true;	
		double binary = frac[k];
		double step = binary;
		for(int i = 0; i < 2; i++){
			asple_nov[i] = s[i].testASPLpure(a[i]);
			if(asple_nov[i] > 0){
				continuesearch = true;
				asple_nov_test = false;} 
		}
		if(asple_nov_test){
			for(int i = 0; i < 2; i++){
				asple_nov[i + 2] = s[i].testNPOVpure(a[i], tod);
				if(asple_nov[i + 2] < 0){
					continuesearch = true;
					asple_nov_test = false;} 
			}
		}
		// keep the flows after 1st basket of value
		Side[] ttrial = new Side[2];
		for (int i = 0; i < 2; i++){
			ttrial[i]= (Side) t[i].cloneDeep();
		}
		double devAllow = sa.getOPSLimit() ;
		// start the binary search
	    while (continuesearch) { 
			step = 0.5 * step;
			if (asple_nov_test == true) binary +=step; 
			else binary -= step;	
			asple_nov_test = true;
			for(int i = 0; i < 2; i++){
				s[i] = (Side) origS[i].cloneDeep(); // take back original account
				t[i] = (Side) ttrial[i].cloneDeep(); // take back original side of tr
				t[i].basket((frac[k] - binary) * tot[i], sa.currenciesPerLiquidity(false));
			}
			for(int i = 0; i < 2; i++){
				t[(i + 1) % 2].cleanFlows(false);
				t[(i+1) % 2].copyFlows(t[i], -1, true);
			} 
			for(int i = 0; i < 2; i++){
				s[i].net(t[i]);
				asple_nov[i] = s[i].testASPLpure(a[i]);
				if (asple_nov[i] > 0) {asple_nov_test = false;}
			}
			for(int i = 0; i < 2; i++){
				asple_nov[i + 2] = s[i].testNPOVpure(a[i], tod);
				if (asple_nov[i + 2] < 0) {asple_nov_test = false;}
			}
			if(asple_nov_test == true && ((-asple_nov[0]  < devAllow) || (-asple_nov[1] < devAllow)
										  || (asple_nov[2] < devAllow) || (asple_nov[3] < devAllow))) {
				continuesearch = false;
			}
			if((asple_nov_test == false) && (binary < minFp)){
				continuesearch = false;
			    return null;
			}
		}
		if (binary == 1) return this;
		String splitRef = Transaction.getChild(this.getReference());
		Transaction comp = new Transaction(splitRef, this.getParentReference(),a[0], t[0], a[1], t[1], TransactionType.LCF , Side.OUT , Status.SETTLED);
		comp.setTable(this.getTable());	
		return comp;
	}
		
	public double getApproximateSettlementAmount(){
		double settlAmount1to2 = 0;
		double settlAmount2to1 = 0;
		for(Iterator e = this.legs(); e.hasNext();){
			Leg l = (Leg)e.next();
			Currency c = l.getCurrency();
			double am = l.getAmount();
			// if dir is true, a1 is debited
			if (l.isDir() == true){
				settlAmount1to2 += am * c.getRate();
			}else{
				settlAmount2to1 += am * c.getRate();
			}
		}
		return (settlAmount1to2 + settlAmount2to1) / 2;
	}
	
	public Side getFirstSide(){
		Side s = new Side();
		for(Iterator e = legs(); e.hasNext();){
			Leg l = (Leg)e.next();
			double am = l.getAmount();
			boolean dir = l.isDir();
			s.setFlow(l.getCurrency(), (dir == true) ? -am : am);
		}
		return s;
	}
	
	public Side getSecondSide(){
		Side s = new Side();
		for(Iterator e = legs(); e.hasNext();){
			Leg l = (Leg)e.next();
			double am = l.getAmount();
			boolean dir = l.isDir();
			s.setFlow(l.getCurrency(), (dir == true) ? am : -am);
		}
		return s;
	}
	
	public boolean areValidAccounts(Account x, Account y){
		return ((x == a1 && y == a2) || (x == a2 && y == a1));
	}
	
	public void addLeg(Leg l){
		Currency ccy = l.getCurrency();
		Leg ll = this.getLeg(ccy);
		if(ll != null){
			ll.addAmount(l.isDir(), l.getAmount());
		}else{
			this.setLeg(l);
		}
	}
	
	public void addTransaction(Transaction t) throws SQLException {
		if (!areValidAccounts(t.getFirstAccount(), t.getSecondAccount())){
			throw new SQLException("Incompatible accounts in Transaction::addTransaction");
		}
		for(Iterator e = this.legs(); e.hasNext();){
			this.addLeg((Leg)e.next());
		}
	}
	
	
	public void inv(){
		for(Iterator e = this.legs(); e.hasNext();){
			((Leg)e.next()).inv();
		}
	}
	
	public void multiply(double m){
		for(Iterator e = this.legs(); e.hasNext();){
			((Leg)e.next()).multiply(m);
		}
	}

	public void divide(double m){
		for(Iterator e = this.legs(); e.hasNext();){
			((Leg)e.next()).divide(m);
		}
	}

	public void normalize(){
		for(Iterator e = this.legs(); e.hasNext();){
			((Leg)e.next()).normalize();
		}
	}
	
	public static String getChild(String ref){
		char sep = '/';
		int child = 1;
		String s = null;
		try {
			int where = ref.lastIndexOf(sep);
			if (where != -1){
				String sub = ref.substring(where + 1);
				String core = ref.substring(0,where +1);
				child =  Integer.parseInt(sub);
				child += 1;
				s = core + String.valueOf(child);
			} else {
				s = ref + sep +  String.valueOf(child);
			}
		} catch (NumberFormatException ex){
			ex.printStackTrace();
		} 
		return s;
	}

}