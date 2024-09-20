package legacyb3.core;

import legacyb3.or2.TimeOfDay;

import java.sql.SQLException;

public class Movement extends ClsObject {
	private String ref;
	private String parentRef;
	private MovementType type;
	private Account db;
	private Account cr;
	private Currency ccy;
	private double am;
	private TimeOfDay ts;
	
	// constructors
	
	public Movement(String ref, String parentRef, MovementType type, Account a1, Account a2, Currency c, double am){
		this.setReference(ref);
		this.setParentReference( parentRef);
		this.setMovementType(type);
		this.setDBAccount(a1);
		this.setCRAccount(a2);
		this.setCurrency(c);
		this.setAmount(am);
	}
	
	public Movement(){
		this("", "_top_", null, null, null, null, 0);
	}
	
	// accessors
	
	public void setReference(String ref){
		this.ref = ref;
	}
	
	public String getReference(){
		return ref;
	}
	
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
	
	public void setAccount(Account a){
		this.setDBAccount(a);
	}
	
	public Account getAccount(){
		return this.getDBAccount();
	}
	
	public void setDBAccount(Account a){
		this.db = a;
	}
	
	public Account getDBAccount(){
		return db;
	}
	
	public void setOtherAccount(Account a){
		this.setCRAccount(a);
	}
	
	public Account getOtherAccount(){
		return this.getCRAccount();
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
	
	public void setBook(TimeOfDay ts){
		this.ts = ts;
	}
	
	public TimeOfDay getBook(){
		return this.ts;
	}
	
	// JAVA support
	
	public Object clone(){
		Movement m = (Movement)super.clone();
		return m;
	}
	
	public String toString(){
		return "[ref=" + ref + ", parent=" + parentRef + ", type=" + type.getID() + ", DB=" + db.getID() + ", CR=" + cr.getID() + ", ccy=" + ccy.getID() + ", am=" + am + "]";
	}
	
	// miscellaneous
	
	public void book() throws SQLException{
		this.book(new TimeOfDay(0));
	}
	
	public void book(TimeOfDay ts) throws SQLException{
		setBook(ts);
		if(!db.getAccountType().equals(type.getFromAccountType()))
			throw new SQLException("Illegal movement. Wrong DB account type.");
		if(!cr.getAccountType().equals(type.getToAccountType()))
			throw new SQLException("Illegal movement. Wrong CR account type.");
		BalanceType bt = type.getBalanceType();
		db.post(bt, ccy, -am);
		cr.post(bt, ccy, am);
		if(type.isSave()) save();
	}
	
	public void inv(){
		Account a = db;
		db = cr;
		cr = a;
	}
	
}
	
	