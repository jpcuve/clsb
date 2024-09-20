package legacyb3.core;

import legacyb3.data.*;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.TimeOfDay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;

public class ClsSimulatorModel extends java.util.Observable implements AppListener{ 
	
	private TimeOfDay now;
	private TimeOfDay last;
	
	private AppListener al = null;
	
	private MessageProxy mp;
	private Parties pat;
	private AccountTypes att;
	private Accounts at;
	private CurrencyGroups cgt;
	private Currencies ct;
	private BalanceTypes btt;
	private Balances bt;
	private Globals gt;
	private Transactions tt;
	private Queue qo;
	private MovementTypes mtt;
	private Movements mt;
	private PayOuts pt;
	private PayIns pi;
	private PayInSchedules pist;
	private TrialPayInSchedules tpist;
	private AccountCurrencies act;
	private RTGSCutOffs cot;
	private Inputs it;
	private AuthorizationQueue aut;
	private PayOutAlgorithm poa;
	private PayInAlgorithm pia;
	private PayInDeadlines pids;
	private PayInPercentages pips;
	private UMPositionsWithSM umPos;
	private UMBalances umBals;
	
	// constructors
	
	public ClsSimulatorModel() throws ClassNotFoundException, SQLException{
		Class.forName ("com.ms.jdbc.odbc.JdbcOdbcDriver");
	}
	
	
	// accessors
	
	public void setTimeOfDay(TimeOfDay tod){
		if(tod.isBefore(this.last)) this.now = this.last;
		else this.now = tod;
		this.setChanged();
		this.notifyObservers();
	}
	
	public TimeOfDay getTimeOfDay(){
		return this.now;
	}
			
	public void setLast(TimeOfDay tod){
		this.last = tod;
		this.setChanged();
		this.notifyObservers();
	}
	
	public TimeOfDay getLast(){
		return this.last;
	}
			
	public MessageProxy getMessageProxy(){
		return this.mp;
	}
	
	public Global getGlobal(){
		return gt.get();
	}
	
	public Globals getGlobals(){
		return this.gt;
	}
	
	public Parties getParties(){
		return this.pat;
	}
	
	public AccountTypes getAccountTypes(){
		return this.att;
	}
	
	public Accounts getAccounts(){
		return this.at;
	}
	
	public CurrencyGroups getCurrencyGroups(){
		return this.cgt;
	}
	
	public Currencies getCurrencies(){
		return this.ct;
	}
	
	public BalanceTypes getBalanceTypes(){
		return this.btt;
	}
	
	public Balances getBalances(){
		return this.bt;
	}
	
	public Transactions getTransactions(){
		return this.tt;
	}
	
	public Queue getSettlementQueue(){
		return this.qo;
	}
	
	public Movements getMovements(){
		return this.mt;
	}
	
	public PayOuts getPayOuts(){
		return this.pt;
	}
	
	public PayIns getPayIns(){
		return this.pi;
	}
	
	public PayInSchedules getPayInSchedules(){
		return this.pist;
	}
	
	public TrialPayInSchedules getTrialPayInSchedules(){
		return tpist;
	}
	
	public AccountCurrencies getAccountCurrencies(){
		return this.act;
	}
	
	public RTGSCutOffs getRTGSCutOffs(){
		return this.cot;
	}
	
	public Inputs getInputs(){
		return this.it;
	}
	
	public AuthorizationQueue getAuthorizationQueue(){
		return this.aut;
	}
	
	public PayInDeadlines getPayInDeadlines(){
		return this.pids;
	}
	
	public PayInPercentages getPayInPercentages(){
		return this.pips;
	}
	public UMPositionsWithSM getUMPositionsWithSM(){
		return this.umPos;
	}
	public UMBalances getUMBalances(){
		return this.umBals;
	}
	
	// AppListener overload
	
	public void addAppListener(AppListener l){
		al = (AppListener)AppEventMulticaster.add(al, l);
	}
	
	public void removeAppListener(AppListener l){
		al = (AppListener)AppEventMulticaster.remove(al, l);
	}
		
	public void appMessage(AppEvent e){
		if(al != null) al.appMessage(e);
	}
	
	public void appProgress(AppEvent e){
		if(al != null) al.appProgress(e);
	}
	
	public void appTime(AppEvent e){
		if(al != null) al.appTime(e);
	}
	
	public void progress(String s, int p){
		if(al != null) al.appProgress(new AppEvent(this, s, p));
	}
	
	// core functions
	
	private void loadCachedServers() throws SQLException{
		pat.addAppListener(this);
		att.addAppListener(this);
		at.addAppListener(this);
		cgt.addAppListener(this);
		ct.addAppListener(this);
		btt.addAppListener(this);
		bt.addAppListener(this);
		mtt.addAppListener(this);
		pist.addAppListener(this);
		act.addAppListener(this);
		cot.addAppListener(this);
		pids.addAppListener(this);
		pips.addAppListener(this);
		umPos.addAppListener(this);
		// umBals.addAppListener(this);
		try{
			gt.load();
			pat.load();
			att.load();
			at.load();
			cgt.load();
			ct.load();
			btt.load();
			bt.load();
			mtt.load();
			pids.load();
			pist.load();
			act.load();
			cot.load();
			pips.load();
			umPos.load();
			// umBals.load();
		}catch(SQLException ex){
			this.appProgress(new AppEvent(this, "", 0));
			throw ex;
		}
		pat.removeAppListener(this);
		att.removeAppListener(this);
		at.removeAppListener(this);
		cgt.removeAppListener(this);
		ct.removeAppListener(this);
		btt.removeAppListener(this);
		bt.removeAppListener(this);
		mtt.removeAppListener(this);
		pist.removeAppListener(this);
		act.removeAppListener(this);
		cot.removeAppListener(this);
		pids.removeAppListener(this);
		pips.removeAppListener(this);
		umPos.removeAppListener(this);
		// umBals.removeAppListener(this);			
		al.appProgress(new AppEvent(this, "Static data loaded.", 0));
		
		for(Iterator i = cgt.currencyGroupsPerPriority(true); i.hasNext();) System.out.println(i.next());
		for(DBEnumeration e1 = pat.parties(PartyType.UM); e1.hasMoreElements();){
			UM um = (UM) e1.nextElement();
			System.out.println(um);
			for(Iterator e2 = um.pos(); e2.hasNext();){
				System.out.println("  " + e2.next());
			}
		}
	}
		
	public void reset(String odbc) throws SQLException{
		Connection con = DriverManager.getConnection("jdbc:odbc:" + odbc);
		this.mp = new MessageProxy();
		this.gt = new Globals(con);
		this.pat = new Parties(con);
		this.att = new AccountTypes(con);
		this.at = new Accounts(con, att, pat);
		this.cgt = new CurrencyGroups(con);
		this.ct = new Currencies(con, cgt, at);
		this.btt = new BalanceTypes(con);
		this.bt = new Balances(con, at, btt, ct);
		this.mtt = new MovementTypes(con, att, btt);
		this.mt = new Movements(con, mtt, ct, at);
		this.pt = new PayOuts(con, ct, at);
		this.pi = new PayIns(con, ct, at, mt);
		this.tt = new Transactions(con, "Trs", "TrLegs", ct, at, mt);
		this.qo = new Queue(con, "SettlementQueue", "SettlementQueueLegs", ct, at, mt);
		this.pids = new PayInDeadlines(con, cgt);
		this.pist = new PayInSchedules(con, at, ct, pids);
		this.tpist = new TrialPayInSchedules(con, at, ct, pids);
		this.act = new AccountCurrencies(con, ct, at);
		this.cot = new RTGSCutOffs(con, ct);
		this.it = new Inputs(con, "Inputs", "InputDeals", ct, pat);
		this.aut = new AuthorizationQueue(con, "AuthorizationQueue", "AuthorizationQueueDeals", ct, pat);
		this.pips = new PayInPercentages(con, ct, pids);
		this.umPos = new UMPositionsWithSM(con,pat);
		this.umBals = new UMBalances(con,umPos,pat,btt,ct);
		loadCachedServers();
		SettlementAlgorithm.instance(gt, ct);
		poa = PayOutAlgorithm.instance(gt, ct);
		pia = PayInAlgorithm.instance(gt, ct);
		TimeOfDay tod = gt.get().getTimeOfDay();
		this.setTimeOfDay(tod);
		this.setLast(tod);
		/*
		try{
			java.util.Date d = new java.util.Date();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSSS");
			FileOutputStream fos = new FileOutputStream(odbc + " " + fmt.format(d) + ".log");
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		*/
	}
	
	public void freeze() throws SQLException{
		this.setLast(this.getTimeOfDay());
		this.getGlobal().setTimeOfDay(this.getTimeOfDay());
		this.getGlobals().flush();
	}
	
	public void loadTrialPayInSchedules() throws SQLException{
		pist.load();
		mp.add("------ Loading trial pay-in schedules into accounts");
		tpist.loadIntoCurrentPayInSchedules();
		pist.save();
		pist.notifyObservers();
		mp.add("------ End loading trial pay-in schedules into accounts");
	}
	
	public void copyPayInSchedulesToPayIns(String pistyp) throws SQLException{
		pist.load();
		pi.clear();
		for(DBEnumeration e1 = pist.payInSchedules(pistyp); e1.hasMoreElements();){
			PayInSchedule pis = (PayInSchedule)e1.nextElement();
			PayIn p = new PayIn(pis.getAccount(), pis.getCurrency(), Status.READY, pis.getPayInDeadline().getTimeOfDay(), pis.getAmount());
			pi.save(p);
		}
		pi.notifyObservers();
	}
	
	public void computeCurrentPayInSchedules() throws SQLException {
		/*
		bt.load();
		pist.load();
		*/
		mp.add("------ Computing current pay-in schedules");
		at.addAppListener(this);
		at.computePayInSchedules(pia, now, false);
		at.removeAppListener(this);
		this.freeze();
		pist.save();
		pist.notifyObservers();
		mp.add("------ End computing current pay-in schedules");
	}
	
	public void computeInitialPayInSchedules() throws SQLException {
		/*
		bt.load();
		pist.load();
		*/
		mp.add("------ Computing initial pay-in schedules");
		at.addAppListener(this);
		at.computePayInSchedules(pia, now, true);
		at.removeAppListener(this);
		this.freeze();
		pist.save();
		pist.notifyObservers();
		mp.add("------ End computing initial pay-in schedules");
	}
	
	public void checkBalances() throws SQLException {
		// bt.load();
		mp.add("------ Start checking consistency of balances");
		for(DBEnumeration e1 = ct.currencies(); e1.hasMoreElements();){
			Currency ccy = (Currency)e1.nextElement();
			double sumSTD = 0;
			double sumOPN = 0;
			for(DBEnumeration e2 = at.settlementMemberAccounts(); e2.hasMoreElements();){
				Account a = (Account)e2.nextElement();
				sumSTD -= a.getCurrentBalance(ccy);
				sumOPN -= a.getOpenBalance(ccy);
			}
			System.out.println("STD: " + ccy.getID() + " " + ccy.round(ccy.getMirrorAccount().getCurrentBalance(ccy)) + " " + sumSTD);
			System.out.println("OPN: " +ccy.getID() + " " + ccy.round(ccy.getMirrorAccount().getOpenBalance(ccy)) + " " + sumOPN);
			if(ccy.round(ccy.getMirrorAccount().getCurrentBalance(ccy)) != ccy.round(sumSTD)){
				mp.add("Imbalance of current balances for currency " + ccy.getID());	
			}
			if(ccy.round(ccy.getMirrorAccount().getOpenBalance(ccy)) != ccy.round(sumOPN)){
				mp.add("Imbalance of open balances for currency " + ccy.getID());
			}
		}
		mp.add("------ End checking consistency of balances");
	}

	public void assembleAuthorizationQueue() throws SQLException{
		mp.add("------ Start of assembly of authorization queue: " + now);
		aut.addAppListener(this);
		aut.assemble(it, now);
		aut.removeAppListener(this);
		this.freeze();
		mp.add("------ End of assembly of authorization queue: " + now);
		it.notifyObservers();
		aut.notifyObservers();
	}
	
	public void AuthoriseSequentially() throws SQLException{
		mp.add("------ Start of authorisation sequentially: " + now);
		// bt.load();
		aut.addAppListener(this);
		umBals.addAppListener(this);
		aut.processAutomaticAuthorisation(aut.loadAuthorisationQueueInMemory(), false);
		this.freeze();
		mp.add("------ End of authorisation sequentially");
		System.out.println("Finished authorization");
		this.umBals.save();
		aut.notifyObservers();
		umBals.notifyObservers();
		umBals.removeAppListener(this);
		aut.removeAppListener(this);
	}
	public void StartIPSIDForAuthorisation() throws SQLException{
		mp.add("------ Start of Authorisation processes for the IPSID event: " + now);
		mp.add("------ Reset UM balances: " + now);
		aut.addAppListener(this);
		umBals.addAppListener(this);
		UMBalance umb = null;
		for(DBEnumeration e1 = umBals.UMBalances(BalanceType.std); e1.hasMoreElements();){
			umb = (UMBalance)e1.nextElement();
			umb.setAmount(0);
		}
		umBals.save();
		umBals.notifyObservers();	
		if (aut != null) { 
			mp.add("------ Remove Manual Authorise Flags: " + now);
			aut.clearManualAuthorisationFlags();
			aut.notifyObservers();
		}
		umBals.removeAppListener(this);
		aut.removeAppListener(this);
		mp.add("------ Start of Authorisation processes for the IPSID event: " + now);
	}
	
	public void AuthoriseSDTSequentially() throws SQLException{
		mp.add("------ Start of authorisation sequentially after IPISD: " + now);
		// bt.load();
		aut.addAppListener(this);
		umBals.addAppListener(this);
		aut.processAutomaticAuthorisation(aut.loadAuthorisationQueueInMemory(), true);
		this.freeze();
		mp.add("------ End of authorisation sequentially");
		this.umBals.save();
		aut.notifyObservers();
		umBals.notifyObservers();
		umBals.removeAppListener(this);
		aut.removeAppListener(this);
	}
	
	
	public void assembleTransactionQueue() throws SQLException{
		/*
		bt.load();
		pist.load();
		*/
		mp.add("------ Start of assembly of transaction queue: " + now);
		tt.addAppListener(this);
		tt.build(aut);
		tt.removeAppListener(this);
		mp.add("------ End of assembly of transaction queue: " + now);
		/*
		mp.add("------ Computing initial pay-in schedules: " + now);
		at.computePayInSchedules(pia, now, true);
		mp.add("------ End computing initial pay-in schedules: " + now);
		*/
		this.freeze();
		bt.save();
		// pist.save();
		aut.notifyObservers();
		tt.notifyObservers();
		bt.notifyObservers();
		// pist.notifyObservers();
	}
	
	public void assembleSettlementQueue() throws SQLException{
		// bt.load();
		mp.add("------ Start of assembly of settlement queue: " + now);
		qo.addAppListener(this);
		qo.assemble(tt);
		qo.removeAppListener(this);
		this.freeze();
		bt.save();
		mp.add("------ End of assembly of settlement queue: " + now);
		tt.notifyObservers();
		qo.notifyObservers();
		mt.notifyObservers();
		bt.notifyObservers();
	}
	
	public void clearMovements() throws SQLException {
		mt.clear();
		mt.notifyObservers();
	}
	
	public void clearPayOuts() throws SQLException{
		pt.clear();
		pt.notifyObservers();
	}
	
	public void clearSettlementQueue() throws SQLException{
		qo.clear();
		qo.notifyObservers();
	}
	/*
	public void SODUMBalances() throws SQLException {
		umPos.load();
		umBals.load();
		UMBalance umb = null;
		for(DBEnumeration e1 = umBals.UMBalances(BalanceType.std); e1.hasMoreElements();){
			umb = (UMBalance)e1.nextElement();
			umb.setAmount(0);
		}
		for(DBEnumeration e1 = umBals.UMBalances(BalanceType.opn); e1.hasMoreElements();){
			umb = (UMBalance)e1.nextElement();
			UMPositionWithSM umPos = umb.getUMPos();
			Currency c = umb.getCurrency();
			umPos.setAmount(BalanceType.std,c,umb.getAmount());
		}
		umBals.save();
		umBals.notifyObservers();
	}
	*/
	/** Start of day procedures
	 * These include:
	 * - Clearing balances (except limits and opening) and pay-in schedules
	 * - Copying opening balances to current balances
	 * - Clearing movements
	 * - Clearing pay-outs
	 * - Resetting pay-ins
	 * - Resetting inputs
	 * - Clearing the authorization queue
	 * - Clearing the transaction queue
	 * - Clearing the settlement queue
	 * - ... Preparing UM balances
	 */
	public void balancesSOD() throws SQLException{
		int nsteps = 12;
		int step = 0;
		mp.add("------ Start of day");
		this.appProgress(new AppEvent(this, "Clearing balances and pay-in schedules", 0));
		pist.clear();
		pist.notifyObservers();
		for(DBEnumeration e1 = bt.balances(); e1.hasMoreElements();){
			Balance b = (Balance)e1.nextElement();
			BalanceType btyp = b.getBalanceType();
			if(!btyp.isSOD()) bt.erase(b);
		}
		this.appProgress(new AppEvent(this, "Copying opening balances to current balances", (++step * 100) / nsteps));
		// bt.load();
		for(DBEnumeration e1 = at.accounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			a.sod();
		}
		for(DBEnumeration e1 = bt.balances(); e1.hasMoreElements();){
			Balance b = (Balance)e1.nextElement();
			if(b.getBalanceType() == BalanceType.opn){
				// Balance bn = new Balance(b.getAccount(), BalanceType.std, b.getCurrency(), b.getAmount());
				Balance bn = (Balance)b.clone();
				bn.setBalanceType(BalanceType.std);
				b.getAccount().setBalance(bn);
			}
		}
		bt.save();
		bt.notifyObservers();
		this.appProgress(new AppEvent(this, "Clearing UM balances", (++step * 100) / nsteps));
		for(DBEnumeration e1 = umBals.UMBalances(); e1.hasMoreElements();){
			UMBalance b = (UMBalance)e1.nextElement();
			BalanceType btyp = b.getBalanceType();
			if(!btyp.isSOD()) umBals.erase(b);
		}
		this.appProgress(new AppEvent(this, "Copying opening UM balances to current UM balances", (++step * 100) / nsteps));
		// bt.load();
		for(DBEnumeration e1 = umPos.UMPositionsWithSM(); e1.hasMoreElements();){
			UMPositionWithSM us = (UMPositionWithSM)e1.nextElement();
			us.sod();
		}
//		delete_me();
		for(DBEnumeration e1 = umPos.UMPositionsWithSM(); e1.hasMoreElements();){
			UMPositionWithSM us = (UMPositionWithSM)e1.nextElement();
			System.out.println(us);
			UM um = us.getUM();
			System.out.println(um);
			/*
			for(Enumeration ee = um.pos(); ee.hasMoreElements();){
				System.out.println("--" + ee.nextElement());
			}
			*/
			for(DBEnumeration e2 = umBals.UMBalances(us); e2.hasMoreElements();){
				UMBalance b = (UMBalance)e2.nextElement();
				System.out.println("  " + b);
				if(b.getBalanceType() == BalanceType.opn){
					// Balance bn = new Balance(b.getAccount(), BalanceType.std, b.getCurrency(), b.getAmount());
					UMBalance bn = (UMBalance)b.clone();
					bn.setBalanceType(BalanceType.std);
					b.getUMPos().setBalance(bn);
				}
			}
		}
		// delete_me();
		umBals.save();
		umBals.notifyObservers();
		this.progress("Clearing movements", (++step * 100) / nsteps);
		mt.clear();
		mt.notifyObservers();
		this.progress("Clearing pay-outs", (++step * 100) / nsteps);
		pt.clear();
		pt.notifyObservers();
		this.progress("Resetting pay-ins", (++step * 100) / nsteps);
		pi.ready();
		pi.notifyObservers();
		this.progress("Resetting inputs", (++step * 100) / nsteps);
		it.unmatch();
		it.notifyObservers();
		this.progress("Clearing authorization queue", (++step * 100) / nsteps);
		aut.clear();
		aut.notifyObservers();
		this.progress("Clearing transaction queue", (++step * 100) / nsteps);
		tt.clear();
		tt.notifyObservers();
		this.progress("Clearing settlement queue", (++step * 100) / nsteps);
		qo.clear();
		qo.notifyObservers();
		this.progress("Start of day", (++step * 100) / nsteps);
		TimeOfDay tod = new TimeOfDay(0, 0);
		this.setLast(tod);
		this.setTimeOfDay(tod);
		this.freeze();
		this.progress("Start of day.", 0);
		mp.add("------ Finished start of day");
	}
	
	private void delete_me() throws SQLException {
		for(DBEnumeration e1 = pat.parties(PartyType.UM); e1.hasMoreElements();){
			UM um = (UM)e1.nextElement();
			System.out.println(um);
			for(Iterator e2 = um.pos(); e2.hasNext();){
				UMPositionWithSM us = (UMPositionWithSM)e2.next();
				System.out.println("  " + us);
				for(Iterator e3 = us.umBalances(); e3.hasNext();){
					System.out.println("    " + e3.next());
				}
			}
		}
	}
	
	public void bookPayIns() throws SQLException{
		// bt.load();
		mp.add("------ Start of book pay-in: " + now);
		pi.addAppListener(this);
		int i = pi.book(now);
		pi.removeAppListener(this);
		this.freeze();
		mp.add("Booked " + i  + " pay-ins");
		mp.add("------ End of book pay-in");
		bt.save();
		pi.notifyObservers();
		bt.notifyObservers();
	}

	public void settleOnce(boolean failureManagement) throws SQLException{
		mp.add("------ Start of settle once: " + now);
		// bt.load();
		qo.addAppListener(this);
		int i = qo.rejectObsoleteTransactions(now);
		mp.add("Rejected " + i + " obsolete transactions");
		i = qo.processSequentially(failureManagement, now);
		qo.removeAppListener(this);
		mp.add("Settled " + i  + " transactions");
		this.freeze();
		mp.add("------ End of settle once");
		bt.save();
		qo.notifyObservers();
		bt.notifyObservers();
		mt.notifyObservers();
	}
	
	public void settleSequentially(boolean failureManagement) throws SQLException{
		mp.add("------ Start of settle sequentially: " + now);
		// bt.load();
		int i = qo.rejectObsoleteTransactions(now);
		mp.add("Rejected " + i + " obsolete transactions");
		i = 0;
		// qo.processSequentially(failureManagement, now);//pha
		while((i = qo.processSequentially(failureManagement, now)) != 0){
			mp.add("Settled " + i  + " transactions");
		}
		this.freeze();
		mp.add("------ End of settle sequentially");
		bt.save();
		qo.notifyObservers();
		bt.notifyObservers();
		mt.notifyObservers();
	}
	
	public void payOut() throws SQLException {
		// load balances and pay-in schedules
		mp.add("------ Start of payout: " + now);
		// bt.load();
		int rej = qo.rejectObsoleteTransactions(now);
		mp.add("Rejected " + rej + " obsolete transactions");
		act.load();
		pist.load();
		// compute available payout for all accounts
		/*
		mp.add("Recalculating pay-in schedules");
		pist.load();
		at.computePayInSchedules(pia, now, false);
		pist.save();
		pist.notifyObservers();
		*/
		mp.add("Paying out");
		at.addAppListener(this);
		at.computeAvailablePayOutValues(poa, now);
		at.createPayOuts(poa, now, pt, mt);
		at.removeAppListener(this);
		// save balances
		this.freeze();
		bt.save();
		// pist.save();
		// notify obsevers
		mp.add("------ End of payout");
		mp.notifyObservers();
		bt.notifyObservers();
		mt.notifyObservers();
		pt.notifyObservers();
	}

		
}
	