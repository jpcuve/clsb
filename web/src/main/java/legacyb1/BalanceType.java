package legacyb1;

import java.util.*;

public class BalanceType extends DBRecord{
	private String id;
	private String name;
	private Hashtable balances;
	
	// constructors
	
	public BalanceType(String id, String name){
		this.id = id;
		this.name = name;
		this.balances = new Hashtable();
	}
	
	public BalanceType(String id){
		this(id, null);
	}
	
	// accessors
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setBalance(Balance b){
		String key = b.getBalanceType().getID() + b.getCurrency().getName();
		balances.put(key, b);
	}
	
	public Balance getBalance(BalanceType bt, Currency ccy){
		String key = bt.getID() + ccy.getName();
		return (Balance)balances.get(key);
	}
	
	public Enumeration balances(){
		return balances.elements();
	}
	
	
	// JAVA support
	
	public Object clone(){
		return new BalanceType(id, name);
	}
	
	public String toString(){
		return "[id=" + id + ", name=" + name + "]";
	}
	
	// miscellaneous
	
}