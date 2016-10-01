package legacyb3.core;

import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import legacyb3.or2.TimeOfDay;
import legacyb3.data.*;
import legacyb3.util.*;

public class Account extends ClsObject implements Cloneable {
	private String id;
	private AccountType actyp;
	private Affiliate af;
	private boolean neligible;
	private double aspl;
	private double nccollateral;
	private boolean suspendedForPayOut;
	private double avpo;
	protected TreeMap bals;
	protected TreeMap pis;
	protected TreeMap acs;
	
	public final static int NET = 0;
	public final static int SHORT = 1;
	public final static int LONG = 2;
	
	// constructors

	public Account(String id, AccountType actyp, Affiliate af, boolean neligible, double aspl, double ncc, boolean suspendedForPayOut){
		this.setID(id);
		this.setAccountType(actyp);
		this.setAffiliate(af);
		if (neligible) setNotEligible(); else clearNotEligible();
		setASPL(aspl);
		setNonCashCollateral(ncc);
		if (suspendedForPayOut) setSuspendedForPayOut(); else clearSuspendedForPayOut();
		this.avpo = 0;
		this.bals = new TreeMap();
		this.pis = new TreeMap();
		this.acs = new TreeMap();
	}
	
	public Account(String id){
		this(id, null, null, false, 0, 0, false);
	}
	
	// accessors
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public void setAccountType(AccountType actyp){
		this.actyp = actyp;
	}
	
	public AccountType getAccountType(){
		return actyp;
	}
	
	public void setAffiliate(Affiliate af){
		this.af = af;
	}
	
	public Affiliate getAffiliate(){
		return this.af;
	}
	
	public void setNotEligible(){
		this.neligible = true;
	}
	
	public void clearNotEligible(){
		this.neligible = false;
	}
	
	public boolean isNotEligible(){
		return neligible;
	}
	
	public boolean isEligible(){
		return !this.neligible;
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
	
	public void setSuspendedForPayOut(){
		this.suspendedForPayOut = true;
	}
	
	public void clearSuspendedForPayOut(){
		this.suspendedForPayOut = false;
	}
	
	public boolean isSuspendedForPayOut(){
		return suspendedForPayOut;
	}
	
	public boolean isSuspendedForPayOut(Currency ccy){
		AccountCurrency accy = this.getAccountCurrency(ccy);
		if (accy != null) return accy.isSuspendedForPayOut();
		return false;
	}
	
	public boolean isEligible(Currency ccy){
		AccountCurrency accy = this.getAccountCurrency(ccy);
		if (accy != null) return !accy.isNotEligible();
		return true;
	}

	public void setAvailablePayOut(double avpo){
		this.avpo = avpo;
	}
	
	public double getAvailablePayOut(){
		return this.avpo;
	}
	
	public void setPayInScheduleFlow(PayInSchedule pi) {
		String key = pi.getPayInScheduleType() + pi.getCurrency().getID() + pi.getPayInDeadline().getID();
		this.pis.put(key, pi);
		this.setChanged();
		this.notifyObservers();
	}
	
	public PayInSchedule getPayInScheduleFlow(String pisTyp, Currency ccy, PayInDeadline pid){
		String key = pisTyp + ccy.getID() + pid.getID();
		return (PayInSchedule)pis.get(key);
	}
	
	public void setPayInSchedule(String pisTyp, Currency ccy, CashFlow cf)  {
		for(Iterator i1 = ccy.getCurrencyGroup().payInDeadlines(); i1.hasNext();){
			PayInDeadline pid = (PayInDeadline)i1.next();
			PayInSchedule pis = this.getPayInScheduleFlow(pisTyp, ccy, pid);
			double am = (cf == null) ? 0 : cf.getFlow(pid.getTimeOfDay());
			if(pis != null){
				pis.setAmount(am);
			}else{
				this.setPayInScheduleFlow(new PayInSchedule(this, ccy, pid, pisTyp, am));
			}
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	public CashFlow getPayInSchedule(String pisTyp, Currency ccy){
		CashFlow cf = new CashFlow();
		for(Iterator i1 = ccy.getCurrencyGroup().payInDeadlines(); i1.hasNext();){
			PayInDeadline pid = (PayInDeadline)i1.next();
			PayInSchedule pis = this.getPayInScheduleFlow(pisTyp, ccy, pid);
			if(pis != null)	cf.setFlow(pid.getTimeOfDay(), pis.getAmount());
		}
		return (cf.size() == 0) ? null : cf;
	}
	
	public CashFlow getCurrentPayInSchedule(Currency ccy){
		return this.getPayInSchedule(PayInScheduleType.CURRENT, ccy);
	}
	
	public CashFlow getPreviousPayInSchedule(Currency ccy){
		return this.getPayInSchedule(PayInScheduleType.PREVIOUS, ccy);
	}
	
	public CashFlow getInitialPayInSchedule(Currency ccy){
		return this.getPayInSchedule(PayInScheduleType.INITIAL, ccy);
	}
	
	public void setBalance(Balance b) throws SQLException{
		String key = b.getBalanceType().getID() + b.getCurrency().getID();
		this.bals.put(key, b);
		this.setChanged();
		this.notifyObservers();
	}
	
	public Balance getBalance(BalanceType bt, Currency ccy){
		String key = bt.getID() + ccy.getID();
		return (Balance)bals.get(key);
	}
	
	public void setAmount(BalanceType bt, Currency ccy, double am) throws SQLException {
		Balance b = getBalance(bt, ccy);
		if(b != null) b.setAmount(am);
		else setBalance(new Balance(this, bt, ccy, am));
		this.setChanged();
		this.notifyObservers();
	}
	
	public double getAmount(BalanceType bt, Currency ccy){
		Balance b = getBalance(bt, ccy);
		return (b == null) ? 0 : b.getAmount();
	}

	/**
	 * Get current balance for a specified <code>ccy</code>.
	 * 
	 * @param	ccy	the currency for which the current balance is retrieved.
	 * @return	amount
	 */
	
	public double getCurrentBalance(Currency ccy){
		return getAmount(BalanceType.std, ccy);
	}
	
	/**
	 * Get current balance for currency <code>ccy</code> at time <code>now</code>.
	 * 
	 * @param	ccy	the currency for which the current balance is retrieved.
	 * @param	now the time. If after currency close, long balance is returned as zero.
	 * @return	amount
	 */
	
	public double getCurrentBalance(Currency ccy, TimeOfDay now){
		double curr = this.getCurrentBalance(ccy);
		if(now.isAfter(ccy.getRTGSClose()) && curr > 0) curr = 0;
		return curr;
	}
	
	/**
	 * Get open balance for a specified <code>ccy</code>. The open balance is the initial balance of the settlement day.
	 * It is copied into the current balance as part of the start of day procedures.
	 * 
	 * @param	ccy	the currency for which the open balance is retrieved.
	 * @return	amount
	 */
	
	public double getOpenBalance(Currency ccy){
		return getAmount(BalanceType.opn, ccy);
	}
	
	/**
	 * Get pay-in balance for a specified <code>ccy</code>.
	 * 
	 * @param	ccy	the currency for which the pay-in balance is retrieved.
	 * @return	amount
	 */
	
	public double getPayInBalance(Currency ccy){
		return getAmount(BalanceType.pin, ccy);
	}
	
	/**
	 * Get pay-out balance for a specified <code>ccy</code>.
	 * 
	 * @param	ccy	the currency for which the pay-out balance is retrieved.
	 * @return	amount
	 */
	
	public double getPayOutBalance(Currency ccy){
		return getAmount(BalanceType.pou, ccy);
	}
	
	/**
	 * Get short position limit for a specified <code>ccy</code>.
	 * 
	 * @param	ccy	the currency for which the current balance is retrieved.
	 * @return	amount
	 */
	
	public double getSPL(Currency ccy){
		return getAmount(BalanceType.spl, ccy);
	}
	
	public double getUnsettledBalance(Currency ccy){
		return getAmount(BalanceType.uns, ccy);
	}
	
	public double getUnassembledBalance(Currency ccy){
		return getAmount(BalanceType.una, ccy);
	}
	
	public double getPNP(Currency ccy, TimeOfDay now, boolean initial){
		// System.out.println("Arkadi2: curr bal=" + this.getCurrentBalance(ccy, now) + ", un-balance=" + ((initial) ? this.getUnassembledBalance(ccy) : this.getUnsettledBalance(ccy)));
		return this.getCurrentBalance(ccy, now) + ((initial) ? this.getUnassembledBalance(ccy) : this.getUnsettledBalance(ccy));
	}
	/*
	public double getPNP(Currency ccy, TimeOfDay now){
		return this.getOpenCurrentBalance(ccy, now) + this.getUnsettledBalance(ccy);
	}
	
	public double getEarlyPNP(Currency ccy, TimeOfDay now){
		return getOpenCurrentBalance(ccy, now) + getUnassembledBalance(ccy);
	}
	*/
	public double getNetCashBalance(TimeOfDay now){
		return getOpenPosition(NET, true, now);
	}
	
	public void setAccountCurrency(AccountCurrency accy){
		acs.put(accy.getCurrency().getID(), accy);
	}
	
	public AccountCurrency getAccountCurrency(Currency ccy){
		return (AccountCurrency)acs.get(ccy.getID());
	}
	
	public Side getSide(){
		Side s = new Side();
		for(Iterator i1 = this.currenciesCurrent(); i1.hasNext();){
			Currency ccy = (Currency)i1.next();
			s.setFlow(ccy, this.getCurrentBalance(ccy));
		}
		return s;
	}
	
	public Iterator balances(){
		return this.bals.values().iterator();
	}
	
	public Iterator payInSchedules(){
		return this.pis.values().iterator();
	}
	
	public Iterator accountCurrencies(){
		return acs.values().iterator();
	}
	
	public Iterator currencies(){
		return new AccountCurrencyIterator(this);
	}
	
	public Iterator currenciesShort(){
		return new AccountCurrencyIterator(this, Account.SHORT );
	}
	
	public Iterator currenciesLong(){
		return new AccountCurrencyIterator(this, Account.LONG );
	}
	
	public Iterator currenciesCurrent(){
		return new AccountCurrencyIterator(this, Account.NET );
	}
	
	public Iterator currenciesPayIn(){
		return new AccountCurrencyIterator(this, BalanceType.pin, Account.NET );
	}
	
	public Iterator currenciesShort(BalanceType btyp){
		return new AccountCurrencyIterator(this, btyp, Account.SHORT );
	}
	
	public Iterator currenciesLong(BalanceType btyp){
		return new AccountCurrencyIterator(this, btyp, Account.LONG );
	}
	
	// JAVA support
	
	public String toString(){
		return "[" + id + ", eligible=" + !neligible + ", ASPL=" + aspl + ", NCcollateral=" + nccollateral + ", susp PO=" + suspendedForPayOut + ", pos=" + getPosition(NET, false) + "]";
	}
	
	public int hashCode(){
		return id.hashCode();
	}
	
	public boolean equals(Account a){
		return a.getID().equals(id);
	}
	
	public Object clone(){
		Account a = (Account)super.clone();
		a.bals = (TreeMap)this.bals.clone();
		a.pis = (TreeMap)this.pis.clone();
		a.acs = (TreeMap)this.acs.clone();
		return a;
	}
	
	// miscellaneous
	
	public void sod(){
		this.bals.clear();
		this.pis.clear();
	}
	
	public double post(BalanceType bt, Currency ccy, double am) throws SQLException {
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
	
	public boolean hasPaidIn(){
		boolean hasPI = false;
		for(Iterator i1 = this.currenciesPayIn(); i1.hasNext() && !hasPI;){
			Currency ccy = (Currency)i1.next();
			if(this.getPayInBalance(ccy) < 0) hasPI = true;
		}
		return hasPI;
	}

	public double getPosition(int type, boolean volatilityMargin){
		double shortPosition = 0;
		double longPosition = 0;
		for(Iterator i1 = this.balances(); i1.hasNext();){
			Balance b = (Balance)i1.next();
			if(b.getBalanceType() == BalanceType.std){
				Currency ccy = b.getCurrency();
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
		}		
		switch(type){
		case SHORT: return shortPosition;
		case LONG: return longPosition;
		}
		return (longPosition - shortPosition);
	}
	
	public double getOpenPosition(int type, boolean volatilityMargin, TimeOfDay now){
		double shortPosition = 0;
		double longPosition = 0;
		for(Iterator i1 = this.balances(); i1.hasNext();){
			Balance b = (Balance)i1.next();
			if(b.getBalanceType() == BalanceType.std){
				Currency ccy = b.getCurrency();
				double bal = getCurrentBalance(ccy, now);
				double vm = ccy.getVolatilityMargin();
				double factor = volatilityMargin ? ((bal < 0) ? (1 + vm) : (1 - vm)) : 1;
				double rate = ccy.getRate() * factor;
				if (bal > 0){
					longPosition += bal * rate;
				}else{
					shortPosition -= bal * rate;
				}	
			}
		}
		switch(type){
		case SHORT: return shortPosition;
		case LONG: return longPosition;
		}
		return (longPosition - shortPosition);
	}
	
	public double getExpectedShortPosition(Currency ccy, TimeOfDay now, TimeOfDay fut){
		// System.out.println("Arkadi: start compute esp");
		double esp = 0;
		if(now.isAfterOrEqual(ccy.getRTGSOpen()) && now.isBefore(ccy.getRTGSClose())){
			CashFlow cf = getCurrentPayInSchedule(ccy);
			if(cf != null){
				// System.out.println("Arkadi: pay-in schedule=" + cf);
				double schedNow = cf.getCumulatedAmount(now);
				// System.out.println("Arkadi: pay-in schedule at now " + now + "=" + schedNow);
				double schedFut = cf.getCumulatedAmount(fut);
				// System.out.println("Arkadi: pay-in schedule at fut " + fut + "=" + schedNow);
				double schedTot = cf.getTotalAmount();
				double paidIn = -this.getPayInBalance(ccy);
				// System.out.println("Arkadi: paid-in at " + now + "=" + paidIn);
				double espCur = 0;
				if(paidIn < schedNow) esp = schedTot - paidIn;
				else if (paidIn > schedNow) esp = Math.min(schedTot - schedFut, schedTot - paidIn);
				else esp = schedTot - schedFut;
			}
		}else{
			esp = -this.getCurrentBalance(ccy, now);
		}
		// System.out.println("Arkadi: end compute esp");
		return esp;
	}
	
	public double getCoverForEarlierClosingCurrencies(PayOutAlgorithm poa, TimeOfDay now, TimeOfDay fctt){
		// System.out.println("Arkadi: start compute ecc");
		double cmult = poa.getPayOutMultiplier();
		double totalespmtm = 0;
		double maxvm = 0;
		for(Enumeration e1 = poa.currenciesPerLiquidity(true); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			if(c.getFCTT().isAfter(fctt)){
				double vm = c.getVolatilityMargin();
				if(this.getPNP(c, now, false) < 0){
					double esp = this.getExpectedShortPosition(c, now, fctt);
					// System.out.println("Arkadi: curr=" + c.getID() + ", pnp negative, esp=" + esp);
					double rate = c.getRate();
					double mult = 1 + vm * cmult;
					double espmtm = esp * mult * rate;
					totalespmtm += espmtm;
				}
				if(this.getPNP(c, now, false) > 0){
					// System.out.println("Arkadi: curr=" + c.getID() + ", pnp positive");
					maxvm = Math.max(maxvm, vm);
				}
			}
			// System.out.println("Arkadi: total esp mtm=" + totalespmtm);
			// System.out.println("Arkadi: max vm=" + maxvm);
			// System.out.println("Arkadi: multiplier=" + cmult);
		}
		// System.out.println("Arkadi: end compute ecc");
		return totalespmtm * (1 + maxvm * cmult);
	}
	
	public void computeAvailablePayOutValue(PayOutAlgorithm poa, TimeOfDay now){
		TimeOfDay sctt = poa.getSCTT();
		TimeOfDay base = now;
		TimeOfDay end = (base.isAfter(sctt)) ? base : sctt;
		double shortMargin = 0;
		double longMargin = 0;
		double mmult = poa.getPayOutMultiplier();
		double maxVM = 0;
		System.out.println("Computing esp, now=" + now + ", fut=" + end);
		for(Iterator i1 = this.currencies(); i1.hasNext();){
			Currency c = (Currency)i1.next();
			double esp = getExpectedShortPosition(c, base, end);
			System.out.println("  " + c.getID() + ", esp=" + esp + ", pnp=" + getPNP(c, now, false));
			double vm = c.getVolatilityMargin();
			double rate = c.getRate();
			if (this.getPNP(c, now, false) < 0){ // "Short"
				shortMargin += esp * rate * vm * mmult;
				longMargin += esp * rate;
			}
			if (this.getPNP(c, now, false) > 0) // "Long"
			 	maxVM = Math.max(maxVM, vm);
		}
		longMargin *= maxVM * mmult;
		System.out.println("Short Margin=" + shortMargin);
		System.out.println("Long Margin=" + longMargin);
		double coverForFinalPosition = shortMargin + longMargin;
		
		System.out.println("Cover for final position=" + coverForFinalPosition);
		double pmult = poa.getPayOutMultiplier();
		double netPayAmount = 0;
		System.out.println("Unsettled positions");
		for(Iterator i1 = this.currencies(); i1.hasNext();){
			Currency c = (Currency)i1.next();
			double unsettledPosition = getUnsettledBalance(c);
			double vm = c.getVolatilityMargin();
			double rate = c.getRate();
			double mult = (unsettledPosition > 0) ? (1 - vm * pmult) : (1 + vm * pmult);
			netPayAmount += unsettledPosition * rate * mult;
			System.out.println("  " + c.getID() + " " + unsettledPosition + " , equiv=" + unsettledPosition * rate * mult);
		}
		double coverForUnsettled = - Math.min(0, netPayAmount);
		System.out.println(this);
		System.out.println("Cover for unsettled transactions=" + coverForUnsettled);
		double reserveBalance = coverForFinalPosition + coverForUnsettled;
		System.out.println("Reserve balance=" + reserveBalance);
		double availablePayout = Math.max(0, getOpenPosition(NET, false, now) - reserveBalance);
		System.out.println("Available payout=" + availablePayout);
		setAvailablePayOut(availablePayout);
		System.out.println();
	}
	
	private CashFlow computeStandardPayInSchedule(TimeOfDay now, Currency ccy, double total, CashFlow prev){
		CashFlow cf = new CashFlow();
		double minPI = ccy.getMinimumPayIn();
		double cumul = 0;
		for(Iterator i1 = ccy.payInTimes(); i1.hasNext() && cumul < total;){
			TimeOfDay tod = (TimeOfDay)i1.next();
			double perc = ccy.getPercentage(tod);
			double am = 0;
			if(total <= minPI) am = minPI;
			else am = ccy.round(perc * total) - cumul;
			if(tod.isBefore(now) && prev != null) am = prev.getCumulatedAmount(tod) - cumul;
			cf.addFlow(tod, am);
			cumul += am;
		}
		return cf;
	}
	
	
	private class PisCcy{
		private Currency ccy;
		private double esp;
		
		public PisCcy(Currency ccy, double esp){
			this.setCurrency(ccy);
			this.setESP(esp);
		}
		
		public void setCurrency(Currency ccy){
			this.ccy = ccy;
		}
		
		public Currency getCurrency(){
			return this.ccy;
		}
		
		public void setESP(double esp){
			this.esp = esp;
		}
		
		public double getESP(){
			return this.esp;
		}
	}
	
	public void computePayInSchedule(PayInAlgorithm pia, TimeOfDay now, boolean initial) {
		System.out.println("Computing pay-in schedule for account=" + this.getID() + ", time=" + now);
		// create hashtable with eligible short projected currencies - expected short positions
		HashMap esps = new HashMap();
		for(Iterator i1 = this.currencies(); i1.hasNext();){
			Currency ccy = (Currency)i1.next();
			if(ccy.isEligible() && this.getPNP(ccy, now, initial) < 0) esps.put(ccy, new Double(0));
		}
		// create hashtable with new pay-in schedules
		HashMap pis = new HashMap();
		
		// step 3
		for(Iterator i1 = this.currencies(); i1.hasNext();){
			Currency ccy = (Currency)i1.next();
			if(ccy.isEligible()){
				System.out.println("  Currency=" + ccy.getID());
				// step 4
				double pnp = this.getPNP(ccy, now, initial);
				// step 6
				double payable = pnp + this.getPayInBalance(ccy);
				payable = (payable < 0) ? -payable : 0;
				System.out.println("    Payable=" + payable);
				// step 7
				if(pnp < 0 || !initial){
					// step 8
					CashFlow cf = this.computeStandardPayInSchedule(now, ccy, payable, this.getCurrentPayInSchedule(ccy));
					pis.put(ccy, cf);
					System.out.println(ccy);
					System.out.println(cf);
				}
			}
		}
		// step 9
		TimeOfDay sctt = pia.getSCTT();
		if(now.isBefore(sctt)){
			double asp = 0;
			// step 10
			for(Iterator e1 = esps.keySet().iterator(); e1.hasNext();){
				Currency ccy = (Currency)e1.next();
				System.out.println("  Currency projected short=" + ccy.getID());
				// step 11
				CashFlow cf = (CashFlow)pis.get(ccy);
				double esp = cf.getTotalAmount() - cf.getCumulatedAmount(sctt);
				System.out.println("    esp=" + esp);
				// step 12
				double spl = this.getSPL(ccy);
				double splExcess = (esp < spl) ? 0 : esp - spl;
				// step 13
				if(splExcess > 0){
					// step 14
					cf.adjust(sctt, splExcess);
					System.out.println(ccy);
					System.out.println(cf);
					// step 15
					esp = cf.getTotalAmount() - cf.getCumulatedAmount(sctt);
				}
				esps.put(ccy, new Double(esp));
				double espMTMvm = esp * ccy.getRate() * (1 + ccy.getVolatilityMargin());
				asp += espMTMvm;
			}
			// step 16
			System.out.println("asp=" + asp);
			double aspl = this.getASPL();
			double asplExcess = (asp < aspl) ? 0 : asp - aspl;
			// step 17
			if(asplExcess > 0){
				// step 19
				for(Iterator e1 = esps.keySet().iterator(); e1.hasNext();){
					Currency ccy = (Currency)e1.next();
					System.out.println("  Currency projected short=" + ccy.getID());
					// step 18
					double spreadAsplExcessMTM = ((Double)esps.get(ccy)).doubleValue() * ccy.getRate() * (1 + ccy.getVolatilityMargin()) / asp * asplExcess;
					System.out.println("  spreadAsplExcessMTM=" + spreadAsplExcessMTM);
					// step 20
					double spreadAsplExcess = spreadAsplExcessMTM / ccy.getRate();
					// step 21
					CashFlow cf = (CashFlow)pis.get(ccy);
					cf.adjust(sctt, spreadAsplExcess);
					System.out.println(ccy);
					System.out.println(cf);
					// step 22
					double esp = cf.getTotalAmount() - cf.getCumulatedAmount(sctt);
					System.out.println(" " + ccy.getID() + " esp=" + esp);
					esps.put(ccy, new Double(esp));
				}
			}
			// step 23
			double encb = 0;
			double pimult = pia.getPayInMultiplier();
			for(Iterator i1 = this.currencies(); i1.hasNext();){
				Currency ccy = (Currency)i1.next();
				Double desp = (Double)esps.get(ccy);
				if(desp == null) encb += (this.getPNP(ccy, now, initial) * ccy.getRate() * (1 - ccy.getVolatilityMargin() * pimult));
				else encb -= (desp.doubleValue() * ccy.getRate() * (1 + ccy.getVolatilityMargin() * pimult));
			}
			System.out.println("encb=" + encb);
			// step 24
			if(encb < 0){
				for(Iterator e1 = esps.keySet().iterator(); e1.hasNext();){
					Currency ccy = (Currency)e1.next();
					System.out.println("  Currency projected short=" + ccy.getID());
					// step 25
					double spreadEncbMTM = -((Double)esps.get(ccy)).doubleValue() * ccy.getRate() * (1 + ccy.getVolatilityMargin()) / asp * encb;
					System.out.println("  spreadEncbMTM=" + spreadEncbMTM);
					// step 27
					double spreadEncb = spreadEncbMTM / ccy.getRate();
					// step 28
					CashFlow cf = (CashFlow)pis.get(ccy);
					cf.adjust(sctt, spreadEncb);
					System.out.println(ccy);
					System.out.println(cf);
				}
			}
		}
		// step 29
		Iterator i1 = pia.fctts(false);
		if(i1.hasNext()){
			TimeOfDay lastFctt = (TimeOfDay)i1.next();
			for(i1 = pia.fctts(true); i1.hasNext();){
				TimeOfDay fctt = (TimeOfDay)i1.next();
				if(fctt.isAfter(now) && !fctt.equals(lastFctt)){
					System.out.println("fctt=" + fctt);
					// step 30
					boolean longPos = false;
					for(Iterator i2 = this.currencies(); i2.hasNext() && !longPos;){
						Currency ccy = (Currency)i2.next();
						if(ccy.getFCTT().equals(fctt) && this.getPNP(ccy, now, initial) > 0){
							System.out.println("Currency " + ccy.getID() + " is long at " + fctt);
							longPos = true;
						}
					}
					if(longPos){
						// step 31
						for(Iterator e2 = esps.keySet().iterator(); e2.hasNext();){
							Currency ccy = (Currency)e2.next();
							// step 32
							CashFlow cf = (CashFlow)pis.get(ccy);
							double esp = cf.getTotalAmount() - cf.getCumulatedAmount(fctt);
							System.out.println("  " + ccy.getID() + " esp=" + esp);
							esps.put(ccy, new Double(esp));
						}
						// step 33
						double encb = 0;
						double pimult = pia.getPayInMultiplier();
						for(Iterator i2 = this.currencies(); i2.hasNext();){
							Currency ccy = (Currency)i2.next();
							Double desp = (Double)esps.get(ccy);
							if(desp == null){
								if(ccy.getFCTT().isAfter(fctt)) encb += (this.getPNP(ccy, now, initial) * ccy.getRate() * (1 - ccy.getVolatilityMargin() * pimult));
							}else{
								encb -= (desp.doubleValue() * ccy.getRate() * (1 + ccy.getVolatilityMargin() * pimult));
							}
						}
						System.out.println("  encb=" + encb);
						// step 34
						if(encb < 0){
							// step 35
							double aspLater = 0;
							for(Iterator e2 = esps.keySet().iterator(); e2.hasNext();){
								Currency ccy = (Currency)e2.next();
								if(ccy.getFCTT().isAfter(fctt)) aspLater += ((Double)esps.get(ccy)).doubleValue() * ccy.getRate() * (1 + ccy.getVolatilityMargin());
							}
							System.out.println("  aspLater(>" + fctt + ")=" + aspLater);
							for(Iterator e2 = esps.keySet().iterator(); e2.hasNext();){
								Currency ccy = (Currency)e2.next();
								// step 37
								if(ccy.getFCTT().isAfter(fctt)){	
									System.out.println("  Currency projected short(>" + fctt + ")=" + ccy.getID());
									// step 36
									double spreadEncbMTM = -((Double)esps.get(ccy)).doubleValue() * ccy.getRate() * (1 + ccy.getVolatilityMargin()) / aspLater * encb;
									System.out.println("  spreadEncbMTM(>" + fctt + ")=" + spreadEncbMTM);
									// step 38
									double spreadEncb = spreadEncbMTM / ccy.getRate();
									CashFlow cf = (CashFlow)pis.get(ccy);
									cf.adjust(fctt, spreadEncb);
									System.out.println(ccy);
									System.out.println(cf);
								}
							}
						}
					}
				}
			}
		}
		// step 39
		for(Iterator e2 = esps.keySet().iterator(); e2.hasNext();){
			Currency ccy = (Currency)e2.next();
			CashFlow cf = (CashFlow)pis.get(ccy);
			System.out.println("Currency=" + ccy.getID());
			cf.smooth(pia.primaryPoints(ccy));
			System.out.println(cf);
			cf.ceil(ccy.getUnit());
			System.out.println(cf);
		}
		// save
		for(Iterator e2 = esps.keySet().iterator(); e2.hasNext();){
			Currency ccy = (Currency)e2.next();
			CashFlow cf = (CashFlow)pis.get(ccy);
			System.out.println("Currency=" + ccy.getID());
			if(initial){
				this.setPayInSchedule(PayInScheduleType.INITIAL , ccy, cf);
			}else{
				CashFlow currcf = this.getPayInSchedule(PayInScheduleType.CURRENT , ccy);
				if(currcf != null) this.setPayInSchedule(PayInScheduleType.PREVIOUS , ccy, currcf);
			}
			this.setPayInSchedule(PayInScheduleType.CURRENT , ccy, cf);
		}
		System.out.println("---------------------------------------------");
	}
	
	public void movePayInSchedules(String from, String to) {
		for(Iterator i1 = this.currencies(); i1.hasNext();){
			Currency ccy = (Currency)i1.next();
			CashFlow cf = this.getPayInSchedule(from, ccy);
			if(cf != null){
				this.setPayInSchedule(to, ccy, cf);
				this.setPayInSchedule(from, ccy, null);
			}
		}
	}
	
}