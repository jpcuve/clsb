package legacyb1;

public class MovementType extends DBRecord{
	private String id;
	private AccountType from;
	private AccountType to;
	private BalanceType bt;
	private String name;
	
	// constructors
	
	public MovementType(String id, AccountType from, AccountType to, BalanceType bt, String name){
		this.id = id;
		this.from = from;
		this.to = to;
		this.bt = bt;
		this.name = name;
	}
	
	public MovementType(String id){
		this(id, null, null, null, "Dummy");
	}
	
	// accessors
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public void setFromAccountType(AccountType act){
		this.from = act;
	}
	
	public AccountType getFromAccountType(){
		return from;
	}
	
	public void setToAccountType(AccountType act){
		this.to = act;
	}
	
	public AccountType getToAccountType(){
		return to;
	}
	
	public void setBalanceType(BalanceType bt){
		this.bt = bt;
	}
	
	public BalanceType getBalanceType(){
		return bt;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	// JAVA support
	
	public Object clone(){
		return new MovementType(id, from, to, bt, name);
	}
	
	public String toString(){
		return "[id=" + id + ", from=" + from.getID() + ", to=" + to.getID() + ", bal typ=" + bt.getID() + ", name=" + name + "]";
	}
	
	public boolean equals(Object o){
		MovementType mt = (MovementType)o;
		return mt.getID().equals(id);
	}
	
	// miscellaneous
	
}