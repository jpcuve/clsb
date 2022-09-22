package legacyb3.core;

public class BalanceType extends ClsObject {
	private String id;
	private String name;
	private boolean sod;
	// private Hashtable balances;
	
	public static BalanceType opn;
	public static BalanceType std;
	public static BalanceType spl;
	public static BalanceType sdl;
	public static BalanceType uns;
	public static BalanceType una;
	public static BalanceType pin;
	public static BalanceType pou;
	
	// constructors
	
	public BalanceType(String id, String name){
		this.id = id;
		this.name = name;
		// this.balances = new Hashtable();
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
	
	public void setSOD(){
		this.sod = true;
	}
	
	public void clearSOD(){
		this.sod = false;
	}
	
	public boolean isSOD(){
		return this.sod;
	}
	/*
	public void setBalance(Balance b){
		String key = b.getBalanceType().getID() + b.getCurrency().getID();
		balances.put(key, b);
	}
	
	public Balance getBalance(BalanceType bt, Currency ccy){
		String key = bt.getID() + ccy.getID();
		return (Balance)balances.get(key);
	}

	public Enumeration balances(){
		return balances.elements();
	}
	*/
	
	// JAVA support
	
	public Object clone(){
		BalanceType bt = (BalanceType)super.clone();
		return bt;
	}
	
	public String toString(){
		return "[id=" + id + ", name=" + name + "]";
	}
	
	// miscellaneous
	
}