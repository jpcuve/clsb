package legacyb1;

import java.util.*;

public class Bundle implements Settlable{
	private Vector transactions;
	
	public Bundle(){
		transactions = new Vector();
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		s.append("[ ");
		for(Enumeration e = transactions(); e.hasMoreElements();){
			Tr t = (Tr)e.nextElement();
			s.append(t.getReference());
			s.append(" ");
		}
		s.append("]");
		return s.toString();
	}

	public void addTransaction(Tr trx){
		transactions.addElement(trx);
	}
	
	public Tr elementAt(int index){
		return (Tr)transactions.elementAt(index);
	}
	
	public Enumeration transactions(){
		return transactions.elements();
	}
	
	public Enumeration chainedGroups(int picksize){
		return new ChainedGroupIterator(this, picksize);
	}
	
	public Enumeration clusters(){
		return new ClusterIterator(this);
	}
	
	public boolean contains(Tr t){
		return transactions.contains(t);
	}
	
	public int size(){
		return transactions.size();
	}
	/*
	public int settleSequentially(Movements mt){
		int settled = 0;
		for(Enumeration e = transactions(); e.hasMoreElements();){
			Settlable s = (Settlable)e.nextElement();
			if (s.qualify(s.getAlgorithm()))	settled++;
		}
		return settled;
	}
	
	public int settleGlobally(){
		return (qualify(getAlgorithm())) ? size() : 0;
	}
	*/
	public SettlementAlgorithm getAlgorithm(){
		return SettlementAlgorithm.allOrNothing;
	}
	
	public Enumeration movements(MovementType mt){
		Vector v = new Vector();
		for(Enumeration e1 = transactions(); e1.hasMoreElements();){
			Tr t = (Tr)e1.nextElement();
			for(Enumeration e2 = t.legs(); e2.hasMoreElements();){
				Leg l = (Leg)e2.nextElement();
				boolean dir = l.getDir();
				Account a1 = t.getFirstAccount();
				Account a2 = t.getSecondAccount();
				Movement m = new Movement(t.getReference(), mt, (dir) ? a1 : a2, (dir) ? a2 : a1, l.getCurrency(), l.getAmount());
				v.addElement(m);
			}
		}
		return v.elements();
	}
		
	
	public Settlable qualify(SettlementAlgorithm settlementAlgorithm){
		Hashtable as = new Hashtable();
		for(Enumeration e = transactions(); e.hasMoreElements();){
			Tr trx = (Tr)e.nextElement();
			for(int i = 0; i < 2; i++){
				Side s1 = (i == 0) ? trx.getFirstSide() : trx.getSecondSide();
				Account a = s1.getAccount();
				Side s2 = (Side)as.get(a);
				if (s2 != null) try{ s1.net(s2); }catch(IllegalAccountException e1){};
				as.put(a, s1);
			}
		}
		for(Enumeration e = as.keys(); e.hasMoreElements();){
			Account a = (Account)e.nextElement();
			Side s = (Side)as.get(a);
			if (!s.testSPL() || !s.testASPL() || !s.testNPOV()) return null;
			// System.out.println(" " + a + "\n" + s);
		}
		return this;
	}

}