package legacyb3.core;

public class SM extends Affiliate {
	
	private Account ac;
	
	public SM(String id){
		super(id);
	}
	
	public SM(String id, String name, Account a){
		super(id, name, "SM");
		this.setAccount(a);
	}
	
	public void setAccount(Account a){
		this.ac = a;
	}
	
	public Account getAccount(){
		return this.ac;
	}
	
}
