package legacyb1;

import java.util.Enumeration;
import java.util.Vector;

public class Tr extends DBRecord{
	private String status;
	private String ref;
	private String parentRef;
	private Account a1;
	private Account a2;
	private Vector legs;
	
	// constructors
	
	protected Tr(String ref, String parentRef, Account a1, Account a2, String st) throws IllegalAccountException{
		status = st;
		this.ref = ref;
		this.parentRef = parentRef;
		if (a1 != a2){
			this.a1 = a1;
			this.a2 = a2;
		}else{
			throw new IllegalAccountException("Same accounts");
		}
		legs = new Vector();
	}
	
	// accessors
	
	public void setFirstAccount(Account a){
		this.a1 = a;
	}
		
	public Account getFirstAccount(){
		return a1;
	}
	
	public void setSecondAccount(Account a){
		this.a2 = a;
	}
		
	public Account getSecondAccount(){
		return a2;
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
	
	
	public Leg getLeg(Currency c){
		for(Enumeration e = legs(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			if (l.getCurrency() == c) return l;
		}
		return null;
	}

	public int size(){
		return legs.size();
	}
	
	public Enumeration legs(){
		return legs.elements();
	}
	
	public Enumeration movements(MovementType mt){
		Vector v = new Vector();
		for(Enumeration e = legs(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			boolean dir = l.getDir();
			Movement m = new Movement(ref, mt, (dir) ? a1 : a2, (dir) ? a2 : a1, l.getCurrency(), l.getAmount());
			v.addElement(m);
		}
		return v.elements();
	}
	
	// JAVA support
	
	public String toString(){
		return "[Ref=" + ref + ", parent=" + parentRef + ", Ac=" + a1.getName() + " " + a2.getName() + ", St=" + status + ", Sa(+/-)=" + getApproximateSettlementAmount() + "]";
	}
	
	public String toStringFull(){
		StringBuffer s = new StringBuffer(1024);
		s.append(toString());
		for(Enumeration e = legs(); e.hasMoreElements();){
			s.append("\n  ");
			s.append(((Leg)e.nextElement()).toString());
		}
		return s.toString();
	}
	
	public Object clone(){
		try{
			Tr t = new Tr(ref, parentRef, a1, a2, status);
			cloneLegs(t);
			return t;
		}catch(IllegalAccountException e2){
			e2.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	protected void cloneLegs(Tr t){
		try{
			for(Enumeration e = legs(); e.hasMoreElements();){
				Leg l = (Leg)((Leg)e.nextElement()).clone();
				l.setTr(t);
				t.net(l);
			};
		}catch(TooManyLegsException e1){
			e1.printStackTrace();
			System.exit(1);
		}
	}
	
	// miscellaneous
	
	public Tr cloneToTr(){
		try{
			Tr t = new Tr(ref, parentRef, a1, a2, status);
			cloneLegs(t);
			return t;
		}catch(IllegalAccountException e2){
			e2.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	public double getApproximateSettlementAmount(){
		double settlAmount1to2 = 0;
		double settlAmount2to1 = 0;
		for(Enumeration e = legs(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			Currency c = l.getCurrency();
			double am = l.getAmount();
			// if dir is true, a1 is debited
			if (l.getDir() == true){
				settlAmount1to2 += am * c.getRate();
			}else{
				settlAmount2to1 += am * c.getRate();
			}
		}
		return (settlAmount1to2 + settlAmount2to1) / 2;
	}
	
	public Side getFirstSide(){
		Side s = new Side(a1);
		for(Enumeration e = legs(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			double am = l.getAmount();
			boolean dir = l.getDir();
			s.net(l.getCurrency(), (dir == true) ? -am : am);
		}
		return s;
	}
	
	public Side getSecondSide(){
		Side s = new Side(a2);
		for(Enumeration e = legs(); e.hasMoreElements();){
			Leg l = (Leg)e.nextElement();
			double am = l.getAmount();
			boolean dir = l.getDir();
			s.net(l.getCurrency(), (dir == true) ? am : -am);
		}
		return s;
	}
	
	public boolean areValidAccounts(Account x, Account y){
		return ((x == a1 && y == a2) || (x == a2 && y == a1));
	}
	
	public void net(Leg l) throws TooManyLegsException {
		Leg ll = getLeg(l.getCurrency());
		if (ll == null){
			legs.addElement(l);
		} else {
			try{
				ll.net(l);
				if (ll.getAmount() == (double)0.0){
					legs.removeElement(ll);
				}
			} catch(IllegalCurrencyException e){
				System.out.println(e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public void net(Tr t) throws IllegalAccountException, TooManyLegsException {
		if (!areValidAccounts(t.getFirstAccount(), t.getSecondAccount())){
			throw new IllegalAccountException();
		}
		for(Enumeration e = t.legs(); e.hasMoreElements();){
			net((Leg)e.nextElement());
		}
	}
	
	public void inv(){
		for(Enumeration e = legs(); e.hasMoreElements();){
			((Leg)e.nextElement()).inv();
		}
	}
	
	public void multiply(double m){
		for(Enumeration e = legs(); e.hasMoreElements();){
			((Leg)e.nextElement()).multiply(m);
		}
	}

	public void normalize(){
		for(Enumeration e = legs(); e.hasMoreElements();){
			((Leg)e.nextElement()).normalize();
		}
	}
	
	public Tr[] split(){
		Tr[] ts = new Tr[1];
		ts[0] = (Tr)clone();
		return ts;
	}


}