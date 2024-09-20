package legacyb1;

public class Movement extends DBRecord {
	private String parentRef;
	private MovementType type;
	private Account db;
	private Account cr;
	private Currency ccy;
	private double am;
	
	// constructors
	
	public Movement(String parentRef, MovementType type, Account a1, Account a2, Currency c, double am){
		this.parentRef = parentRef;
		this.type = type;
		this.db = a1;
		this.cr = a2;
		this.ccy = c;
		this.am = am;
	}
	
	public Movement(){
		this("_top_", null, null, null, null, 0);
	}
	
	// accessors
	
	public void setParentReference(String parentRef){
		this.parentRef = parentRef;
	}
	
	public String getParentReference(){
		return parentRef;
	}
	
	public void setMovementType(MovementType type){
		this.type = type;
	}
	
	public MovementType getMovementType(){
		return type;
	}
	
	public void setDBAccount(Account a){
		this.db = a;
	}
	
	public Account getDBAccount(){
		return db;
	}
	
	public void setCRAccount(Account a){
		this.cr = a;
	}
	
	public Account getCRAccount(){
		return cr;
	}
	
	public void setCurrency(Currency c){
		this.ccy = c;
	}
	
	public Currency getCurrency(){
		return ccy;
	}
	
	public void setAmount(double am){
		this.am = am;
	}
	
	public double getAmount(){
		return am;
	}
	
	// JAVA support
	
	public Object clone(){
		return new Movement(parentRef, type, db, cr, ccy, am);
	}
	
	public String toString(){
		return "[parent=" + parentRef + ", type=" + type.getID() + ", DB=" + db.getName() + ", CR=" + cr.getName() + ", ccy=" + ccy.getName() + ", am=" + am + "]";
	}
	
	// miscellaneous

	public void book() throws IllegalMovementException {
		if(!db.getAccountType().equals(type.getFromAccountType()))
			throw new IllegalMovementException("Illegal movement. Wrong DB account type.");
		if(!cr.getAccountType().equals(type.getToAccountType()))
			throw new IllegalMovementException("Illegal movement. Wrong CR account type.");
		BalanceType bt = type.getBalanceType();
		db.post(bt, ccy, -am);
		cr.post(bt, ccy, am);
	}
	
	public void inv(){
		Account a = db;
		db = cr;
		cr = a;
	}
	
}
	
	