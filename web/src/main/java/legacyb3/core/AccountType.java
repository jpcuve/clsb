package legacyb3.core;

import legacyb3.data.*;
import legacyb3.util.*;

import java.util.Iterator;
import java.util.TreeMap;

public class AccountType extends ClsObject { 

	private String id;
	private String name;
	private TreeMap accounts;
	
	public static AccountType cb;
	public static AccountType sm;
	public static AccountType lp;
	public static AccountType cl;
	public static AccountType su;
	
	// constructors
	
	public AccountType(String id, String name){
		setID(id);
		setName(name);
		accounts = new TreeMap();
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
	
	public void addAccount(Account a){
		accounts.put(a.getID(), a);
	}
	
	// JAVA support
	
	public Object clone(){ // shallow
		AccountType at = (AccountType)super.clone();
		at.accounts = (TreeMap)this.accounts.clone();
		return at;
	}
	
	public String toString(){
		return "[id=" + id + ", name=" + name + "]";
	}
	
	public boolean equals(Object o){
		AccountType at = (AccountType)o;
		return at.getID().equals(id);
	}
	
	// miscellaneous
	
	public Iterator accounts(){
		return accounts.values().iterator();
	}
}