package legacyb3.core;

import legacyb3.or2.TimeOfDay;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Bundle implements Settlable, Bookable {
	private Vector transactions;
	
	public Bundle(){
		transactions = new Vector();
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		s.append("[ ");
		for(Enumeration e = transactions(); e.hasMoreElements();){
			Transaction t = (Transaction)e.nextElement();
			s.append(t.getReference());
			s.append(" ");
		}
		s.append("]");
		return s.toString();
	}

	public void addTransaction(Transaction trx){
		transactions.addElement(trx);
	}
	
	public Transaction elementAt(int index){
		return (Transaction)transactions.elementAt(index);
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
	
	public boolean contains(Transaction t){
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
	public SettlementAlgorithm getSettlementAlgorithm(boolean failureManagement){
		return SettlementAlgorithm.allOrNothing;
	}
	
	// Bookable
	
	public Enumeration movements(){
		Vector v = new Vector();
		for(Enumeration e1 = transactions(); e1.hasMoreElements();){
			Transaction t = (Transaction)e1.nextElement();
			for(Iterator e2 = t.legs(); e2.hasNext();){
				Leg l = (Leg)e2.next();
				boolean dir = l.isDir();
				Account a1 = t.getFirstAccount();
				Account a2 = t.getSecondAccount();
				v.addElement(new Movement(Movement.makeReference("MV"), t.getReference(), MovementType.se, (dir) ? a1 : a2, (dir) ? a2 : a1, l.getCurrency(), l.getAmount()));
			}
		}
		return v.elements();
	}
	
	public Enumeration prMovements(boolean direction){
		Vector v = new Vector();
		for(Enumeration e1 = transactions(); e1.hasMoreElements();){
			Transaction t = (Transaction)e1.nextElement();
			for(Iterator e2 = t.legs(); e2.hasNext();){
				Leg l = (Leg)e2.next();
				boolean dir = l.isDir();
				Account a1 = t.getFirstAccount();
				Account a2 = t.getSecondAccount();
				v.addElement(new Movement(Movement.makeReference("MV"), t.getReference(), MovementType.pr, (dir ^ direction) ? a2 : a1, (dir ^ direction) ? a1 : a2, l.getCurrency(), l.getAmount()));
			}
		}
		return v.elements();
	}
		
	public Enumeration movements(MovementType mt){
		Vector v = new Vector();
		for(Enumeration e1 = transactions(); e1.hasMoreElements();){
			Transaction t = (Transaction)e1.nextElement();
			for(Iterator e2 = t.legs(); e2.hasNext();){
				Leg l = (Leg)e2.next();
				boolean dir = l.isDir();
				Account a1 = t.getFirstAccount();
				Account a2 = t.getSecondAccount();
				Movement m = new Movement(Movement.makeReference("MV"), t.getReference(), mt, (dir) ? a1 : a2, (dir) ? a2 : a1, l.getCurrency(), l.getAmount());
				v.addElement(m);
			}
		}
		return v.elements();
	}
		
	
	public Settlable qualify(SettlementAlgorithm sa, TimeOfDay tod) {
		HashMap as = new HashMap();
		for(Enumeration e = this.transactions(); e.hasMoreElements();){
			Transaction trx = (Transaction)e.nextElement();
			for(int i = 0; i < 2; i++){
				Side s1 = (i == 0) ? trx.getFirstSide() : trx.getSecondSide();
				Account a = (i == 0) ? trx.getFirstAccount() : trx.getSecondAccount();
				Side s2 = (Side)as.get(a);
				if (s2 != null) s1.net(s2);
				as.put(a, s1);
			}
		}
		for(Iterator e = as.keySet().iterator(); e.hasNext();){
			Account a = (Account)e.next();
			Side s = (Side)as.get(a);
			if (!s.testSPL(a) || !s.testASPL(a) || !s.testNPOV(a, tod)) return null;
			// System.out.println(" " + a + "\n" + s);
		}
		return this;
	}

}