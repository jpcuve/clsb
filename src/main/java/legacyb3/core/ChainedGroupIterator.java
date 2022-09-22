package legacyb3.core;

import java.util.Enumeration;
import java.util.Vector;

public class ChainedGroupIterator implements Enumeration {
	private CombinatorialIterator c;
	private Bundle bundle;
	private int ps;
	
	public ChainedGroupIterator(Bundle bundle, int ps){
		int size = bundle.size();
		c = new CombinatorialIterator(bundle.size(), ps);
		this.bundle = bundle;
		this.ps = Math.min(size, ps);
	}
	
	public boolean hasMoreElements(){
		return c.hasMoreElements();
	}
	
	public Object nextElement(){
		boolean ok = false;
		Bundle b = null;
		while(!ok && c.hasMoreElements()){
			int a[] = (int[])c.nextElement();
			// System.out.println("Trying " + c);
			b = new Bundle();
			Vector v = new Vector();
			ok = true;
			for(int i = 0; i < ps; i++){
				Transaction t = bundle.elementAt(a[i]);
				b.addTransaction(t);
				Account a1 = t.getFirstAccount();
				Account a2 = t.getSecondAccount();
				if (i != 0 && v.indexOf(a1) == -1 && v.indexOf(a2) == -1) ok = false;
				v.addElement(a1);
				v.addElement(a2);
			}
		}
		return b;
	}
}