package legacyb3.core;

public class MovementType extends ClsObject {
	private String id;
	private AccountType from;
	private AccountType to;
	private BalanceType bt;
	private String name;
	private boolean save;
	
	public static MovementType in;
	public static MovementType ou;
	public static MovementType pi;
	public static MovementType po;
	public static MovementType pr;
	public static MovementType se;
	public static MovementType pa;
		
	// constructors
	
	public MovementType(String id, AccountType from, AccountType to, BalanceType bt, String name){
		setID(id);
		setFromAccountType(from);
		setToAccountType(to);
		setBalanceType(bt);
		setName(name);
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
	
	public void setAccountType(AccountType act){
		this.setFromAccountType(act);
	}
	
	public AccountType getAccountType(){
		return this.getFromAccountType();
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
	
	public void setSave(){
		this.save = true;
	}
	
	public void clearSave(){
		this.save = false;
	}
	
	public boolean isSave(){
		return this.save;
	}
	
	// JAVA support
	
	public Object clone(){
		MovementType mt = (MovementType)super.clone();
		return mt;
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