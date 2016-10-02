package legacyb3.data;
 
import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import legacyb3.core.*;
import legacyb3.or2.*;
import legacyb3.util.*;
import legacyb3.or2.DBColumn;

public final class Accounts extends CachedServer{
	private DBColumn m_cAccountType;
	private DBColumn m_cAffiliate;
		
	// constructors
	
	public Accounts(Connection con, AccountTypes att, Parties pt) throws SQLException{
		super(con, "Accounts", new Account("Dummy"), "accounts");
		this.addColumnString(true, "Account", "ID");
		m_cAccountType = new DBColumn(this, false, AccountTypeFK.instance(att), "Account type FK", "AccountType");
		this.addColumnDefinition(m_cAccountType);
		m_cAffiliate = new DBColumn(this, false, PartyFK.instance(pt, Affiliate.class), "Party FK", "Affiliate");
		this.addColumnDefinition(m_cAffiliate);
		this.addColumnDouble(false, "Aspl", "ASPL");
		this.addColumnDouble(false, "Non-cash collateral", "NonCashCollateral");
		this.addColumnBoolean(false, "Suspended for pay-out", "SuspendedForPayOut");
		this.checkStructure();
	}
	
	// overloaded.
	
	public void validate(DBRecord rec) throws SQLException{
		Account a = (Account)rec;
		if(a.getAccountType() == null || a.getAffiliate() == null) throw new SQLException(a.getKey() + ApplicationError.isNull );
		if(a.getASPL() < 0) throw new SQLException(a.getKey() + " ASPL" + ApplicationError.isNegativeOrZero );
		a.getAccountType().addAccount(a);
		Affiliate af = a.getAffiliate();
		if(af instanceof SM) ((SM)af).setAccount(a);
		else ((CLSB)af).addAccount(a);
	}
		
	// accessors
	
	public Account get(String name) throws SQLException{
		Account a = (Account)find(new Account(name));
		if (a == null) throw new SQLException("Illegal account");
		return a;
	}
	
	public DBEnumeration accounts() throws SQLException {
		return records();
	}
	
	public DBEnumeration settlementMemberAccounts() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cAccountType, "='" + AccountType.sm.getID() + "'");
		return records(dbf);
	}
	
	public int settlementMemberAccountsSize() throws SQLException{
		DBFilter dbf = new DBFilter(this);
		dbf.setConstraint(m_cAccountType, "='" + AccountType.sm.getID() + "'");
		return size(dbf);
	}
	
	public int availablePayOutAccountsSize() throws SQLException{
		int count = 0;
		for(DBEnumeration e1 = this.settlementMemberAccounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			if(a.getAvailablePayOut() > 0) count++;
		}
		return count;
	}
	
	public Iterator accountsPerAvailablePayOut() throws SQLException{
		return new AccountPayOutIterator(this);
	}
	
	// JAVA support
	
	public String toString(){
		return "Cached accounts=\n" + super.toString();
	}
	
	// miscellaneous
	
	public void computePayInSchedules(PayInAlgorithm pia, TimeOfDay now, boolean initial) throws SQLException{
		this.progress("Computing pay-in schedules", 0);
		int psize = this.settlementMemberAccountsSize();
		int pcount = 0;
		for(DBEnumeration e1 = this.settlementMemberAccounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			pcount++;
			this.progress("Computing pay-in schedule for account " + pcount + " of " + psize, (pcount * 100) / psize);
			a.computePayInSchedule(pia, now, initial);
		}
		this.progress("Computed " + pcount + " pay-in schedules", 0);
		setChanged();
	}

	
	public void computeAvailablePayOutValues(PayOutAlgorithm poa, TimeOfDay now) throws SQLException {
		this.progress("Computing available pay-out values", 0);
		int psize = this.settlementMemberAccountsSize();
		int pcount = 0;
		for(DBEnumeration e1 = this.settlementMemberAccounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			pcount++;
			this.progress("Computing available pay-out value for account " + pcount + " of " + psize, (pcount * 100) / psize);
			System.out.println("Computing available pay-out for account: " + a.getID() + " (" + a.getAffiliate().getID() + ")");
			if(a.isEligible() && !a.isSuspendedForPayOut()){
				a.computeAvailablePayOutValue(poa, now);
			}
		}
		this.progress("Computed " + pcount + " available pay-out values", 0);
		setChanged();
	}
	
	public void createPayOuts(PayOutAlgorithm poa, TimeOfDay now, PayOuts pt, Movements mt) throws SQLException{
		this.progress("Paying out", 0);
		int psize = this.availablePayOutAccountsSize();
		int pcount = 0;
		for(Iterator i1 = this.accountsPerAvailablePayOut(); i1.hasNext();){
			Account a = (Account)i1.next();
			pcount++;
			this.progress("Paying out for account " + pcount + " of " + psize, (pcount * 100) / psize);
			System.out.println("Processing account: " + a.getID() + " (" + a.getAffiliate().getID() + ")");
			if(a.isEligible() && !a.isSuspendedForPayOut() && a.hasPaidIn()){
				HashMap maxpos = new HashMap();
				Iterator i2 = poa.fctts(false);
				if(i2.hasNext()){
					TimeOfDay fctt = (TimeOfDay)i2.next();
					System.out.println("  fctt=" + fctt + " (skipped)");
					while(i2.hasNext()){
						fctt = (TimeOfDay)i2.next();
						System.out.println("  fctt=" + fctt);
						if(fctt.isAfter(now)){
							double cecc = a.getCoverForEarlierClosingCurrencies(poa, now, fctt);
							System.out.println("    cover ecc=" + cecc);
							double totalpnpmtm = 0;
							for(Iterator i3 = a.currencies(); i3.hasNext();){
								Currency c = (Currency)i3.next();
								// System.out.println("Arkadi2: considering currency " + c.getID());
								double pnp = a.getPNP(c, now, false);
								// System.out.println("Arkadi2: pnp=" + pnp);
								// System.out.println("Arkadi2: curr fctt=" + c.getFCTT());
								if(pnp > 0 && c.getFCTT().isAfter(fctt)){
									double pnpmtm = pnp * c.getRate();
									// System.out.println("Arkadi2: curr rate=" + c.getRate());
									System.out.println("      pnp=" + pnpmtm + " (" + c.getID() + " " + pnp + ")");
									totalpnpmtm += pnpmtm;
									// System.out.println("Arkadi2: cumulated mtm pnp=" + totalpnpmtm);
								}
							}
							System.out.println("    total pnp=" + totalpnpmtm);
							double maxpo = Math.max(totalpnpmtm - cecc, 0);
							System.out.println("    maximum payout=" + maxpo);
							// if(!(totalpnpmtm == 0 && cecc == 0)) maxpos.put(fctt, new Double(maxpo));
							maxpos.put(fctt, new Double(maxpo));
						}
					}
				}
				Enumeration e2;
				for(e2 = poa.currenciesPerLiquidity(true); e2.hasMoreElements();){
					Currency c = (Currency)e2.nextElement();
					if(c.isEligible() && !c.isSuspendedForPayOut() && !c.isRTGSCutOff(now) && c.getCC().isAfter(now) && c.getCurrencyGroup().getPriority() != 0 &&
					   a.isEligible(c) && !a.isSuspendedForPayOut(c) && a.getAvailablePayOut() > 0 && a.getCurrentBalance(c, now) > 0){
						System.out.println("Account " + a.getID() + " currency " + c.getID());
						System.out.println("    Current open balance=" + a.getCurrentBalance(c, now));
						System.out.println("    Pnp (>0)=" + Math.max(0, a.getPNP(c, now, false)));
						double mirror = 0;
						for(DBEnumeration e3 = this.settlementMemberAccounts(); e3.hasMoreElements();) mirror += ((Account)e3.nextElement()).getCurrentBalance(c);
						System.out.println("    Mirror account bal= " + mirror);
						double po1 = Math.min(mirror, Math.min(a.getCurrentBalance(c, now), Math.max(0, a.getPNP(c, now, false))));
						// System.out.println("1.1 step 4=" + po1);
						double po1mtm = po1 * c.getRate();
						double minmaxpo = Double.MAX_VALUE;
						for(Iterator e3 = maxpos.keySet().iterator(); e3.hasNext();){
							TimeOfDay fctt = (TimeOfDay)e3.next();
							if(c.getFCTT().isAfter(fctt)){
								System.out.println("    ...Considering fctt=" + fctt);
								double maxpo = ((Double)maxpos.get(fctt)).doubleValue();
								minmaxpo = Math.min(minmaxpo, maxpo);
							}
						}
						System.out.println("    The minimum of the previous 3=" + po1mtm);
						System.out.println("    Minimum of maximum pay-outs=" + minmaxpo);
						System.out.println("    Available pay-out=" + a.getAvailablePayOut());
						double po2 = Math.min(po1mtm, Math.min(minmaxpo, a.getAvailablePayOut()));
						double po = po2 / c.getRate();
						// System.out.println("1.1 step 9=" + po);
						// step 11
						double ncb = a.getNetCashBalance(now);
						System.out.println("    Net cash balance=" + ncb);
						// step 12
						double vm = c.getVolatilityMargin();
						double po2vm = po2 * (1 + vm);
						System.out.println("    Pay-out amount with volatility=" + po2vm);
						// step 13
						boolean test = false;
						for(Enumeration e3 = poa.currenciesPerLiquidity(true); e3.hasMoreElements();){
							Currency c3 = (Currency)e3.nextElement();
							System.out.println("        pnp(" + c3.getID() + ")=" + a.getPNP(c3, now, false));
							if (a.getPNP(c3, now, false) < 0) test = true;
						}
						System.out.println("        test=" + test);
						double excessmtm = po2vm - ncb;
						if(test && excessmtm > 0){
							// step 14, 15 & 16
							double excess = excessmtm / c.getRate() / (1 - vm);
							System.out.println("        excess=" + excess);
							// step 17
							po -= excess;
						}
						// step 20
						if(po != a.getPNP(c, now, false)){
							if(po < c.getMinimumPayOut()) po = 0;
							else po = c.floorUnit(po);
						}
						System.out.println("    Paying out=" + c.getID() + " " + po);
						if(po > 0){
							String ref = PayOut.makeReference("PO");
							PayOut[] p = (new PayOut(ref, a, c, "UN", now, po)).split();
							for(int i = 0; i < p.length; i++){
								mt.book(p[i], now);
								p[i].setStatus(Status.BOOKED);
								pt.save(p[i]);
							}
							double pomtm = po * c.getRate() * (1 + vm);
							a.setAvailablePayOut(a.getAvailablePayOut() - pomtm);
							System.out.println("    New available payout=" + a.getAvailablePayOut());
							for(Iterator e3 = maxpos.keySet().iterator(); e3.hasNext();){
								TimeOfDay fctt = (TimeOfDay)e3.next();
								if(c.getFCTT().isAfter(fctt)){
									double maxpo = ((Double)maxpos.get(fctt)).doubleValue();
									maxpo -= pomtm;
									maxpos.put(fctt, new Double(maxpo));
								}
								System.out.println("      fctt=" + fctt + ", max po=" + maxpos.get(fctt));
							}
						}
					}
				}
			}
			System.out.println();
		}
		this.progress("Paid out " + pcount + " accounts", 0);
	}
	
	
}