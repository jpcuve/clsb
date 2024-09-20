package legacyb1;

import java.util.Enumeration;
import java.util.Vector;

public class BTreeIterator implements Enumeration{
	private Vector v;
	private Enumeration e;
	
	public BTreeIterator(BTree bt, boolean dir){
		v = new Vector();
		bt.copyInto(v, dir);
		e = v.elements();
	}
	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement(){
		return ((BNode)e.nextElement()).getValue();
	}
}