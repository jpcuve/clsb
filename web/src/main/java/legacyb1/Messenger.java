package legacyb1;

import java.util.*;

public class Messenger extends Observable{
	
	private Vector v;
	private Object last;
	
	public Messenger(){
		v = new Vector();
	}
	
	public void add(Object o){
		this.last = o;
		v.addElement(o);
		System.out.println(o);
		setChanged();
		notifyObservers();
	}
	
	public Object getLast(){
		return last;
	}
	
	public Enumeration elements(){
		return v.elements();
	}
}
