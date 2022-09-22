package legacyb3.core;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class GrossTransaction extends Transaction {
	private Currency buyCcy;
	private double buyAm;
	private Currency sellCcy;
	private double sellAm;
	
	// constructors

	protected GrossTransaction(String ref, String parentRef, Account a1, Account a2, String typ, String st, Currency buyCcy, double buyAm, Currency sellCcy, double sellAm){
		super(ref, parentRef, a1, a2, typ, st);
		this.setBuyCurrency(buyCcy);
		this.setBuyAmount(buyAm);
		this.setSellCurrency(sellCcy);
		this.setSellAmount(sellAm);
	}
	
	public GrossTransaction(String ref){
		this(ref, "_top_", null, null, "GT", null, null, 0, null, 0);
	}
	

	// accessors
	
	public final void setBuyCurrency(Currency ccy){
		this.buyCcy = ccy;
	}
	
	public final Currency getBuyCurrency(){
		return this.buyCcy;
	}
	
	public final void setBuyAmount(double am){
		this.buyAm = am;
	}
	
	public final double getBuyAmount(){
		return this.buyAm;
	}
	
	public final void setSellCurrency(Currency ccy){
		this.sellCcy = ccy;
	}
	
	public final Currency getSellCurrency(){
		return this.sellCcy;
	}
	
	public final void setSellAmount(double am){
		this.sellAm = am;
	}
	
	public final double getSellAmount(){
		return this.sellAm;
	}
	
	public void setLeg(Leg l){
		Currency ccy = l.getCurrency();
		double am = l.getAmount();
		if(l.isDir()){ // 1 to 2
			this.setSellCurrency(ccy);
			this.setSellAmount(am);
		}else{
			this.setBuyCurrency(ccy);
			this.setBuyAmount(0);
		}
	}
			
	public Leg getLeg(Currency c){
		if(c.equals(this.getSellCurrency())) return new Leg(this, true, c, this.getSellAmount());
		if(c.equals(this.getBuyCurrency())) return new Leg(this, false, c, this.getBuyAmount());
		return null;
	}
	
	public void addLeg(Leg l){
		Currency ccy = l.getCurrency();
		if(l.isDir() && ccy.equals(this.getSellCurrency())){
			this.setSellAmount(this.getSellAmount() + l.getAmount());
		}else if(!l.isDir() && ccy.equals(this.getBuyCurrency())){
			this.setBuyAmount(this.getBuyAmount() + l.getAmount());
		}
	}
	
	public void addTransaction(Transaction t) throws SQLException{
		if (!areValidAccounts(t.getFirstAccount(), t.getSecondAccount())){
			throw new SQLException("Incompatible accounts in addTransaction");
		}
		Leg l = t.getLeg(this.getSellCurrency());
		if(l != null) this.addLeg(l);
		l = t.getLeg(this.getBuyCurrency());
		if(l != null) this.addLeg(l);
	}

	public int size(){
		return 2;
	}
	
	public Iterator legs(){
		Vector v = new Vector();
		v.addElement(new Leg(this, true, this.getSellCurrency(), this.getSellAmount()));
		v.addElement(new Leg(this, false, this.getBuyCurrency(), this.getBuyAmount()));
		return v.iterator();
	}
	
	public Enumeration movements(MovementType mt, boolean direction){
		Vector v = new Vector();
		Account a1 = this.getFirstAccount();
		Account a2 = this.getSecondAccount();
		String ref = this.getReference();
		v.addElement(new Movement(Movement.makeReference("MV"), ref, mt, direction ? a1 : a2, direction ? a2 : a1, this.getSellCurrency(), this.getSellAmount()));
		v.addElement(new Movement(Movement.makeReference("MV"), ref, mt, direction ? a2 : a1, direction ? a1 : a2, this.getBuyCurrency(), this.getBuyAmount()));
		return v.elements();
	}
	
	// Bookable
	
	public Enumeration movements(){
		Vector v = new Vector();
		Account a1 = this.getFirstAccount();
		Account a2 = this.getSecondAccount();
		String ref = this.getReference();
		v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.se, a1, a2, this.getSellCurrency(), this.getSellAmount()));
		v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.se, a2, a1, this.getBuyCurrency(), this.getBuyAmount()));
		return v.elements();
	}
	
	public Enumeration prMovements(boolean direction){
		Vector v = new Vector();
		Account a1 = this.getFirstAccount();
		Account a2 = this.getSecondAccount();
		String ref = this.getReference();
		v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.pr, direction ? a1 : a2, direction ? a2 : a1, this.getSellCurrency(), this.getSellAmount()));
		v.addElement(new Movement(Movement.makeReference("MV"), ref, MovementType.pr, direction ? a2 : a1, direction ? a1 : a2, this.getBuyCurrency(), this.getBuyAmount()));
		return v.elements();
	}
	
	// JAVA support
	
	public Object clone(){
		GrossTransaction t = (GrossTransaction)super.clone();
		return t;
	}
	
	public Object cloneDeep() throws SQLException {
		return this.clone();
	}

	// miscellaneous
	
	public Transaction[] split() throws SQLException{
		double split = 0;
		for(Iterator e = legs(); e.hasNext();){
			Leg l = (Leg)e.next();
			double div = l.getCurrency().getGrossSplitThreshold();
			if (div != 0) split = Math.max(split, l.getAmount() / div);
		}
		split = Math.floor(split) + 1;
		int size = new Double(split).shortValue();
		GrossTransaction[] ts = new GrossTransaction[size];
		ts[0] = (GrossTransaction)this.cloneDeep();
		if(size > 1){
			GrossTransaction component = (GrossTransaction)cloneDeep();
			component.divide(size);
			component.normalize();
			Transaction subt = (GrossTransaction)component.cloneDeep();
			subt.inv();
			for(int i = 1; i < size; i++){
				ts[i] = (GrossTransaction)component.cloneDeep();
				ts[i].setReference(ts[i].getReference() + "/" + i);
				ts[0].addTransaction(subt);
			}
			ts[0].setReference(ts[0].getReference() + "/0");
		}
		return ts;
	}
	
	public SettlementAlgorithm getSettlementAlgorithm(boolean failureManagement){
		if(failureManagement) return SettlementAlgorithm.optimalPartial;
		return SettlementAlgorithm.allOrNothing;
	}
	
	public double getApproximateSettlementAmount(){
		return (this.getBuyAmount() * this.getBuyCurrency().getRate() + this.getSellAmount() * this.getSellCurrency().getRate()) / 2;
	}

	public Side getFirstSide(){
		Side s = new Side();
		s.setFlow(this.getSellCurrency(), -this.getSellAmount());
		s.setFlow(this.getBuyCurrency(), this.getBuyAmount());
		return s;
	}
	
	public Side getSecondSide(){
		Side s = new Side();
		s.setFlow(this.getSellCurrency(), this.getSellAmount());
		s.setFlow(this.getBuyCurrency(), -this.getBuyAmount());
		return s;
	}
	
	/*
	public void netLeg(Leg l) throws SQLException{
		throw new SQLException("netLeg not implemented for gross transactions");
	}
	
	public void netTransaction(Transaction t) throws SQLException{
		throw new SQLException("netTransaction not implemented for gross transactions");
	}
	*/
	
	public void inv(){
		double am = this.getSellAmount();
		this.setSellAmount(this.getBuyAmount());
		this.setBuyAmount(am);
		Currency ccy = this.getSellCurrency();
		this.setSellCurrency(this.getBuyCurrency());
		this.setBuyCurrency(ccy);
	}
	
	public void multiply(double m){
		this.setSellAmount(this.getSellAmount() * m);
		this.setBuyAmount(this.getBuyAmount() * m);
	}

	public void divide(double m){
		this.setSellAmount(this.getSellAmount() / m);
		this.setBuyAmount(this.getBuyAmount() / m);
	}

	public void normalize(){
	}
	


}
