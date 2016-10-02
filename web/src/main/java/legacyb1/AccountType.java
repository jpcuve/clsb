package legacyb1;

import java.util.*;

public class AccountType extends DBRecord{
	private String id;
	private String name;
	private Hashtable accounts;
	
	// constructors
	
	public AccountType(String id, String name){
		setID(id);
		setName(name);
		accounts = new Hashtable();
	}
	
	public AccountType(String id){
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
	
	public void setAccount(Account a){
		accounts.put(a.getName(), a);
	}
	
	// JAVA support
	
	public Object clone(){
		return new AccountType(id, name);
	}
	
	public String toString(){
		return "[id=" + id + ", name=" + name + "]";
	}
	
	public boolean equals(Object o){
		AccountType at = (AccountType)o;
		return at.getID().equals(id);
	}
	
	// miscellaneous
	
	public Enumeration accounts(){
		return accounts.elements();
	}
}