package legacyb1;

import java.util.*;

public class Account extends DBRecord{
	private String name;
	private AccountType actyp;
	private double aspl;
	private double nccollateral;
	private double avpo;
	private Hashtable balances;
	private Hashtable pis;

	// constructors

	public Account(String name, AccountType actyp, double aspl, double ncc){
		this.name = name;
		this.actyp = actyp;
		this.aspl = aspl;
		this.nccollateral = ncc;
		this.avpo = 0;
		this.balances = new Hashtable();
		this.pis = new Hashtable();
	}
	
	public Account(String name){
		this(name, null, 0, 0);
	}
	
	// accessors
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setAccountType(AccountType actyp){
		this.actyp = actyp;
	}
	
	public AccountType getAccountType(){
		return actyp;
	}
	
	public void setASPL(double aspl){
		this.aspl = aspl;
	}
	
	public double getASPL(){
		return aspl;
	}
	
	public void setNonCashCollateral(double nccollateral){
		this.nccollateral = nccollateral;
	}
	
	public double getNonCashCollateral(){
		return nccollateral;
	}
	
	public void setAvailablePayOut(double avpo){
		this.avpo = avpo;
	}
	
	public double getAvailablePayOut(){
		return this.avpo;
	}
	
	public void setBalance(Balance b){
		String key = b.getBalanceType().getID() + b.getCurrency().getName();
		balances.put(key, b);
		setChanged();
		notifyObservers();
	}
	
	public Balance getBalance(BalanceType bt, Currency ccy){
		String key = bt.getID() + ccy.getName();
		return (Balance)balances.get(key);
	}
	
	public void setAmount(BalanceType bt, Currency ccy, double am){
		Balance b = getBalance(bt, ccy);
		if(b != null) b.setAmount(am);
		else setBalance(new Balance(this, bt, ccy, am));
		setChanged();
		notifyObservers();
	}
	
	public double getAmount(BalanceType bt, Currency ccy){
		Balance b = getBalance(bt, ccy);
		return (b == null) ? 0 : b.getAmount();
	}
	
	public double getAmount(String bt, Currency ccy){
		double bal = 0;
		for(Enumeration e1 = balances(); e1.hasMoreElements();){
			Balance b = (Balance)e1.nextElement();
			if(b.getCurrency().equals(ccy) && b.getBalanceType().getID().equals(bt)){
				bal = b.getAmount();
				break;
			}
		}
		return bal;
	}
	
	public double getCurrentBalance(Currency ccy){
		return getAmount("STD", ccy);
	}
	
	public double getPayInBalance(Currency ccy){
		return getAmount("PIN", ccy);
	}
	
	public double getPayOutBalance(Currency ccy){
		return getAmount("POU", ccy);
	}
	
	public double getSPL(Currency ccy){
		return getAmount("SPL", ccy);
	}
	
	public double getUnsettledBalance(Currency ccy){
		return getAmount("UNS", ccy);
	}
	
	public double getPNP(Currency ccy){
		return getCurrentBalance(ccy) + getUnsettledBalance(ccy);
	}
	
	public double getNetCashBalance(){
		return getPosition("Net", true);
	}
	
	public void setPayInSchedule(Currency c, CashFlow cf){
		pis.put(c, cf);
	}
	
	public CashFlow getPayInSchedule(Currency c){
		return (CashFlow)pis.get(c);
	}
	
	public void setPayInScheduleFlow(PayInSchedule p){
		Currency ccy = p.getCurrency();
		CashFlow cf = getPayInSchedule(ccy);
		if(cf == null){
			cf = new CashFlow();
			setPayInSchedule(ccy, cf);
		}
		cf.addFlow(p.getTime(), p.getAmount());
	}
	
	public Enumeration balances(){
		return balances.elements();
	}
	
	public Enumeration currencies(){
		return new AccountCurrencyIterator(this);
	}
	
	public Enumeration currenciesShort(){
		return new AccountCurrencyIterator(this, "Short");
	}
	
	public Enumeration currenciesLong(){
		return new AccountCurrencyIterator(this, "Long");
	}
	
	public Enumeration currenciesShort(BalanceType btyp){
		return new AccountCurrencyIterator(this, btyp, "Short");
	}
	
	public Enumeration currenciesLong(BalanceType btyp){
		return new AccountCurrencyIterator(this, btyp, "Long");
	}
	
	
	// JAVA support
	
	public String toString(){
		return "[" + name + ", ASPL=" + aspl + ", NCcollateral=" + nccollateral + ", pos=" + getPosition("Net", false) + "]";
	}
	
	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Account a){
		return a.getName().equals(name);
	}
	
	public Object clone(){
		return new Account(name, actyp, aspl, nccollateral);
		// and the rest, JP!
	}

	// miscellaneous
	
	public void printAll(){
		System.out.println(this);
		for(Enumeration e1 = balances(); e1.hasMoreElements();) System.out.println("  " + (Balance)e1.nextElement());
	}
	
	public double post(BalanceType bt, Currency ccy, double am){
		Balance b = getBalance(bt, ccy);
		if(b == null){
			b = new Balance(this, bt, ccy, 0);
			setBalance(b);
		}
		b.post(am);
		setChanged();
		notifyObservers();
		return b.getAmount();
	}

	
	public double getPosition(String p, boolean volatilityMargin){
		double shortPosition = 0;
		double longPosition = 0;
		for(Enumeration e1 = currencies(); e1.hasMoreElements();){
			Currency ccy = (Currency)e1.nextElement();
			double bal = getCurrentBalance(ccy);
			double vm = ccy.getVolatilityMargin();
			double factor = volatilityMargin ? ((bal < 0) ? (1 + vm) : (1 - vm)) : 1;
			double rate = ccy.getRate() * factor;
			if (bal > 0){
				longPosition += bal * rate;
			}else{
				shortPosition -= bal * rate;
			}
		}
		return p.equals("Long") ? longPosition : (p.equals("Short") ? shortPosition : longPosition - shortPosition);
	}
	
	public double getExpectedShortPosition(Currency ccy, TimeOfDay now, TimeOfDay fut){
		double esp = 0;
		CashFlow cf = getPayInSchedule(ccy);
		if(cf != null){
			double schedNow = cf.getCumulatedAmount(now);
			double schedFut = cf.getCumulatedAmount(fut);
			double schedTot = cf.getTotalAmount();
			double paidIn = getPayInBalance(ccy);
			double espCur = 0;
			if(paidIn < schedNow) esp = schedTot - paidIn;
			else if (paidIn > schedNow) esp = Math.min(schedTot - schedFut, schedTot - paidIn);
			else esp = schedTot - schedFut;
		}
		return esp;
	}
	
	public void computeAvailablePayOut(Clsb clsb){
		TimeOfDay sctt = clsb.getSCTT();
		TimeOfDay base = clsb.getCurrent();
		TimeOfDay end = (base.isAfter(sctt)) ? base : sctt;
		double shortMargin = 0;
		double longMargin = 0;
		double mmult = clsb.getMarginMultiplier();
		double maxVM = 0;
		for(Enumeration e1 = currencies(); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			double esp = getExpectedShortPosition(c, base, end);
			System.out.println("esp " + c.getName() + "=" + esp);
			System.out.println("pnp " + c.getName() + "=" + getPNP(c));
			double vm = c.getVolatilityMargin();
			double rate = c.getRate();
			if (getPNP(c) - getPayInBalance(c) < 0) // "Short"
				shortMargin += esp * rate * vm * mmult;
			longMargin += esp * rate;
			if (getPNP(c) + getPayOutBalance(c) > 0) // "Long"
			 	maxVM = Math.max(maxVM, vm);
		}
		longMargin *= maxVM * mmult;
		System.out.println("Short Margin=" + shortMargin);
		System.out.println("Long Margin=" + longMargin);
		double coverForFinalPosition = shortMargin + longMargin;
		
		System.out.println("Cover for final position=" + coverForFinalPosition);
		/*
		for(Enumeration e1 = a.balances(); e1.hasMoreElements();){
			Balance b = (Balance)e1.nextElement();
			System.out.println(b);
		}
		*/
		double pmult = clsb.getPayMultiplier();
		double netPayAmount = 0;
		for(Enumeration e1 = currencies(); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			double unsettledPosition = getUnsettledBalance(c);
			double vm = c.getVolatilityMargin();
			double rate = c.getRate();
			double mult = (unsettledPosition > 0) ? (1 - vm * pmult) : (1 + vm * pmult);
			netPayAmount += unsettledPosition * rate * mult;
			System.out.println("Unsettled position=" + c.getName() + " " + unsettledPosition);
		}
		double coverForUnsettled = - Math.min(0, netPayAmount);
		System.out.println(this);
		System.out.println("Cover for unsettled transactions=" + coverForUnsettled);
		double reserveBalance = coverForFinalPosition + coverForUnsettled;
		System.out.println("Reserve balance=" + reserveBalance);
		double availablePayout = Math.max(0, getPosition("Net",false) - reserveBalance);
		System.out.println("Available payout=" + availablePayout);
		setAvailablePayOut(availablePayout);
	}
		
	public double getCoverForEarlierClosingCurrencies(Clsb clsb, TimeOfDay fctt, Enumeration currencies){
		TimeOfDay now = clsb.getCurrent();
		double cmult = clsb.getCoverMultiplier();
		double totalespmtm = 0;
		double maxvm = 0;
		while(currencies.hasMoreElements()){
			Currency c = (Currency)currencies.nextElement();
			if(c.getFCTT().isAfter(fctt)){ // "Short"
				double vm = c.getVolatilityMargin();
				if(getPNP(c) - getPayInBalance(c) < 0){
					double esp = getExpectedShortPosition(c, now, fctt);
					double rate = c.getRate();
					double mult = 1 + vm * cmult;
					double espmtm = esp * mult * rate;
					totalespmtm += espmtm;
				}
			if(getPNP(c) + getPayOutBalance(c) > 0){ // "Long"
					maxvm = Math.max(maxvm, vm);
				}
			}
		}
		return totalespmtm * (1 + maxvm * cmult);
	}

}