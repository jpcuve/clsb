package legacyb3.core;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


public class UMPositionWithSM extends ClsObject { 
	private SM sm;
	private UM um;
	private double aspl;
	private double crl;
	private double asplSdt;
	private double crlSdt;
	private boolean neligible;
	protected HashMap umBalances;
	
    public static int Short = 0;
	public static int Long = 1;
	public static int net = 2;
	
	// constructor
	public UMPositionWithSM(SM sm,UM um, boolean neligible, double aspl, double crl, double asplSdt, double crlSdt){
		this.setSM(sm);
		this.setUM(um);
		if (neligible) setNotEligible(); else clearNotEligible();
		setASPL(aspl);
		setCRL(crl);
		setASPLSDT(asplSdt);
		setCRLSDT(crlSdt);
		this.umBalances = new HashMap();
	}
	
	public UMPositionWithSM(SM sm, UM um){
		this(sm, um, false, 0, 0, 0,0);
	}
	
	// accessors
	
	public SM getSM(){
		return this.sm;
	}
	
	public void setSM(SM sm){
		this.sm = sm;}
	
	public UM getUM(){
		return this.um;}
	
	public void setUM(UM um){
		this.um = um;}
	
	public double getASPL(){
		return this.aspl;}
	
	public void setASPL(double aspl){
		this.aspl = aspl;}
	
	public double getCRL(){
		return this.crl;}
	
	public void setCRL(double crl){
		this.crl = crl;}
	
	public double getASPLSDT(){
		return this.asplSdt;}
	
	public void setASPLSDT(double asplSdt){
		this.asplSdt = asplSdt;}
	
	public double getCRLSDT(){
		return this.crlSdt;}
	
	public void setCRLSDT(double crlSdt){
		this.crlSdt = crlSdt;}
	
	public void setNotEligible(){
		this.neligible = true;
	}
	
	public void clearNotEligible(){
		this.neligible = false;
	}
	
	public boolean isNotEligible(){
		return this.neligible;
	}
	
	public boolean isEligible(){
		return !this.neligible;
	}
	
	public void setBalance(UMBalance b){
		String key = b.getBalanceType().getID() + b.getCurrency().getID();
		umBalances.put(key, b);
		setChanged();
		notifyObservers();
	}
	public String toString(){
		return "[SM=" + getSM().getID() +  " , UM=" + getUM().getID() + " , ASPL ="
			   + getASPL()+ " , CRL =" + getCRL() + " , ASPL SDT =" + getASPLSDT()+ " , CRL SDT=" + getCRLSDT() + "]";
	}
	public UMBalance getBalance(BalanceType bt, Currency ccy){
		String key = bt.getID() + ccy.getID();
		UMBalance umBal = (UMBalance)umBalances.get(key);
		if(umBal == null){
			umBal = new UMBalance(this,this.getSM(), this.getUM(),bt,ccy,0);
			setBalance(umBal);
		}
		return umBal;
	}
	
	public void setAmount(BalanceType bt, Currency ccy, double am){
		UMBalance b = getBalance(bt, ccy);
		if(b != null) b.setAmount(am);
		else setBalance(new UMBalance(this, this.getSM(), this.getUM(), bt, ccy, am));
		setChanged();
		notifyObservers();
	}
	
	public double getAmount(BalanceType bt, Currency ccy){
		UMBalance b = getBalance(bt, ccy);
		return (b == null) ? 0 : b.getAmount();
	}
	public void setCurrentBalance(Currency ccy, double am){
		UMBalance b = getBalance(BalanceType.std, ccy);
		if(b != null) b.setAmount(am);
		else setBalance(new UMBalance(this, this.getSM(), this.getUM(),BalanceType.std, ccy, am));
		setChanged();
		notifyObservers();
	}
	
	public double getCurrentBalance(Currency ccy){
		UMBalance b = getBalance(BalanceType.std, ccy);
		return (b == null) ? 0 : b.getAmount();
	}
	public void setOpenBalance(Currency ccy, double am){
		UMBalance b = getBalance(BalanceType.opn, ccy);
		if(b != null) b.setAmount(am);
		else setBalance(new UMBalance(this, this.getSM(), this.getUM(),BalanceType.opn, ccy, am));
		setChanged();
		notifyObservers();
	}
	public double getOpenBalance(Currency ccy){
		UMBalance b = getBalance(BalanceType.opn, ccy);
		return (b == null) ? 0 : b.getAmount();
	}
	
	// methods
	public Iterator umBalances(){
		return this.umBalances.values().iterator() ;
	}
	
	public Iterator currencyIterator(){
		return this.umBalances.values().iterator() ;
	}
	
	public Iterator currencies(){
		Iterator e;
		Vector currencies = new Vector();
		for(e = this.umBalances() ; e.hasNext();) {
			Currency ccy = ((UMBalance)e.next()).getCurrency();
			if (!currencies.contains(ccy)){
				currencies.addElement(ccy);}
			}
		e = currencies.iterator();
		return e;}
	
	public double getPosition(int type, boolean volatilityMargin){
		double shortPosition = 0;
		double longPosition = 0;
		for(Iterator e1 = currencies(); e1.hasNext();){
			Currency ccy = (Currency)e1.next();
			double umbal = getAmount(BalanceType.std ,ccy);
			double vm = ccy.getVolatilityMargin();
			double factor = volatilityMargin ? ((umbal < 0) ? (1 + vm) : (1 - vm)) : 1;
			double rate = ccy.getRate() * factor;
			if (umbal > 0){
				longPosition += umbal * rate;
			}else{
				shortPosition -= umbal * rate;
			}
		}
		if(type == UMPositionWithSM.Short) return shortPosition;
		if(type == UMPositionWithSM.Long) return longPosition;
		return (longPosition - shortPosition);
	}
	
	public double getAsplE(){
		// calculate excess : > 0 means excess ; <= 0 means no excess
		return -(getASPL() -getPosition(UMPositionWithSM.Short,true));
	}
	
	public double getSDTAsplE(){
		// calculate excess : > 0 means excess ; <= 0 means no excess
		return -(getASPLSDT() -getPosition(UMPositionWithSM.Short,true));
	}
	
	public double getCrlE(){
		// calculate excess : > 0 means excess ; <= 0 means no excess
		return -(getCRL() + getPosition(UMPositionWithSM.net,true));
	}
	
	public double getSDTCrlE(){
		// calculate excess : > 0 means excess ; <= 0 means no excess
		return -(getCRLSDT() + getPosition(UMPositionWithSM.net,true));
	}
	
	public double getSPLE(Currency ccy){
		double amount = getAmount(BalanceType.std, ccy);
		double spl = getAmount(BalanceType.spl, ccy);
		return -(spl + amount); 
	}
	
	public double getSDTSPLE(Currency ccy){
		double amount = getAmount(BalanceType.std, ccy);
		double spl = getAmount(BalanceType.sdl, ccy);
		return -(spl + amount); 
	}

	public boolean testSPL(){
		for(Iterator e1 = currencies(); e1.hasNext();){
			Currency ccy = (Currency)e1.next();
			if(getSPLE(ccy) > 0) return false;
		}
		return true;
	}
	
	public boolean testSDTSPL(){
		for(Iterator e1 = currencies(); e1.hasNext();){
			Currency ccy = (Currency)e1.next();
			if(getSDTSPLE(ccy) > 0) return false;
		}
		return true;
	}
	
	public boolean testUMLimits(boolean sdt){
		if(sdt){
			// if same day trade => limits SDT (balanceType.sdl)
			if(testSDTSPL())
				if(getSDTAsplE() <= 0)
				if(getSDTCrlE() <= 0) return true;
			return false;
		}
	
		else {
			// if not same day trade => limits (balanceType.spl)
			if(testSPL())
				if(getAsplE() <= 0)
				if(getCrlE() <= 0) return true;
			return false;
		}
	}
	

	
	public void adjustUMPositionWithSM(Input inp){
		for(Iterator e = inp.deals(); e.hasNext();){
			Deal mvt = (Deal)e.next();
			Currency ccy = mvt.getCurrency();
			this.adjustUMPositionWithSM(mvt);
		}
	}
	public void adjustUMPositionWithSM(Deal mvt){
			Currency ccy = mvt.getCurrency();
			double current = getAmount(BalanceType.std,ccy);
			double amount = mvt.getAmount();
			setAmount(BalanceType.std,ccy,current + (((mvt.isSell())?  -1  : 1) * mvt.getAmount())); 
	}
	public void bookSellMvt(Input inp,double factor){
		// Book only sell movements : book factor *  |amount|
		for(Iterator e = inp.deals(); e.hasNext();){
			Deal mvt = (Deal)e.next();
			Currency ccy = mvt.getCurrency();
			if(mvt.isSell())this.bookMvt(mvt, factor);
		}
	}
	public void bookBuyMvt(Input inp, double factor){
		// Book only buy movements : : book factor *  |amount|
		for(Iterator e = inp.deals(); e.hasNext();){
			Deal mvt = (Deal)e.next();
			Currency ccy = mvt.getCurrency();
			if(!(mvt.isSell()))this.bookMvt(mvt, factor);
		}
	}
	public void bookMvt(Deal mvt, double factor){
			Currency ccy = mvt.getCurrency();
			double current = getAmount(BalanceType.std,ccy);
			double amount = mvt.getAmount();
			amount *= factor;
			setAmount(BalanceType.std,ccy,current + amount); 
	}
	
	public Object clone(){
		UMPositionWithSM u = (UMPositionWithSM)super.clone();
		u.umBalances = (HashMap)this.umBalances.clone();
		return u;
	}
	
	public Object cloneDeep(){
		UMPositionWithSM u =  (UMPositionWithSM)this.clone();
		for (Iterator e = umBalances(); e.hasNext();){
			UMBalance b = (UMBalance) e.next();
			UMBalance newB = (UMBalance)b.clone();
			newB.setUMPos(u);
			u.setBalance(newB);
			
		}
		return u;
	}
	
	public void copyBalancesFrom(UMPositionWithSM umPos){
		for(Iterator e1 = umPos.currencies(); e1.hasNext();){
			Currency ccy = (Currency)e1.next();
			this.setCurrentBalance(ccy,umPos.getCurrentBalance(ccy));
		}
	}
	
	public boolean save() throws SQLException{
		super.save();
		for (Iterator e = umBalances(); e.hasNext();){	
			UMBalance b = (UMBalance) e.next();
			b.save();
		}
		return true;
	}
	
	public String toStringFull(){
		StringBuffer s = new StringBuffer(1024);
		s.append(this.toString());
		for(Iterator e = umBalances(); e.hasNext();){
			s.append("\n  ");
			s.append(((UMBalance)e.next()).toString());
		}
		return s.toString();
	}
	
	public void sod(){
		umBalances.clear();
	}

}
