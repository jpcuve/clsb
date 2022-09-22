package legacyb1;

import java.util.Enumeration;
import java.util.Vector;

public class CurrencyGroup extends DBRecord{
	private String name;
	private long priority;
	private Vector currencies;
	
	// constructors
	
	public CurrencyGroup(String name){
		this(name, 0);
	}
	
	public CurrencyGroup(String name, long priority){
		this.name = name;
		this.priority = priority;
		this.currencies = new Vector();
	}
	
	// accessors
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPriority(long priority){
		this.priority = priority;
	}
	
	public long getPriority(){
		return priority;
	}
	
	public void setCurrency(Currency c){
		currencies.addElement(c);
	}
	
	public Enumeration currencies(){
		return currencies.elements();
	}
	
	// JAVA support
	
	public String toString(){
		return "[group=" + name + ", priority=" + priority + "]";
	}
	
	public Object clone(){
		return new CurrencyGroup(name, priority);
	}
	
	public int size(){
		return currencies.size();
	}

}