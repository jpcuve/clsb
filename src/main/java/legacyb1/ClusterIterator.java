package legacyb1;

import java.util.Enumeration;
import java.util.Vector;

public class ClusterIterator implements Enumeration{
	private Bundle bundle;
	private Tr next;
	private Bundle done;
	
	public ClusterIterator(Bundle b){
		bundle = b;
		Enumeration e = bundle.transactions();
		next = (Tr)e.nextElement();
		done = new Bundle();
		done.addTransaction(next);
	}
	
	public boolean hasMoreElements(){
		return (next != null);
	}
	
	public Object nextElement(){
		Bundle b = new Bundle();
		b.addTransaction(next);
		Vector v = new Vector();
		v.addElement(next.getFirstAccount());
		v.addElement(next.getSecondAccount());
		next = null;
		boolean added;
		do{
			added = false;
			for(Enumeration e = bundle.transactions(); e.hasMoreElements();){
				Tr t = (Tr)e.nextElement();
				Account a1 = t.getFirstAccount();
				Account a2 = t.getSecondAccount();
				if (!b.contains(t)){
					if (v.contains(a1) || v.contains(a2)){
						v.addElement(a1);
						v.addElement(a2);
						b.addTransaction(t);
						done.addTransaction(t);
						added = true;
					}else{
						if (!done.contains(t)) next = t;
					}
				}
			}
		}while(added);
		return b;
	}
}