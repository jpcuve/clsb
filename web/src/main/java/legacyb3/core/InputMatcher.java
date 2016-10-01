package legacyb3.core;

import legacyb3.util.*;

import java.util.TreeMap;
import java.util.Vector;

public class InputMatcher{
	private TreeMap tm;
	
	public InputMatcher(){
		this.tm = new TreeMap();
	}
	
	public Vector submit(Input i){
		String femaleKey = i.getFemaleKey();
		Vector iv = (Vector)this.tm.get(femaleKey);
		return iv;
	}
	
	public void add(Input i){
		String maleKey = i.getMaleKey();
		Vector iv = (Vector)this.tm.get(maleKey);
		if(iv == null){
			iv = new Vector();
			this.tm.put(maleKey, iv);
			// System.out.println("InputMatcher size increasing=" + tm.size());
		}
		iv.addElement(i);
	}
	
	public void remove(Input i){
		String maleKey = i.getMaleKey();
		Vector iv = (Vector)this.tm.get(maleKey);
		if(iv != null){
			iv.removeElement(i);
			if(iv.size() == 0){
				this.tm.remove(maleKey);
				// System.out.println("Inputmatcher size decreasing=" + tm.size());
			}
		}
		iv.addElement(i);
	}
	
}
