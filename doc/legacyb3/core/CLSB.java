package legacyb3.core;

import java.util.Enumeration;
import java.util.Vector;

public class CLSB extends Affiliate {
	
	private Vector accounts;
	
	public CLSB(String id){
		super(id);
		this.accounts = new Vector();
	}
	
	public CLSB(String id, String name){
		super(id, name, "CLSB");
		this.accounts = new Vector();
	}
	
	public void addAccount(Account a){
		accounts.addElement(a);
	}
	
	public void removeAccount(Account a){
		accounts.removeElement(a);
	}
	
	public boolean isAccount(Account a){
		return (accounts.indexOf(a) != -1);
	}
	
	public Enumeration accounts(){
		return accounts.elements();
	}
}