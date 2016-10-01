package legacyb3.data;

import java.sql.*;
import java.util.Enumeration;
import java.util.Iterator;

import legacyb3.core.*;
import legacyb3.util.*;
import legacyb3.or2.*;

public class Queue extends Transactions{ 

	public Queue(Connection con, String tableName, String legsTableName, Currencies ct, Accounts at, Movements mt) throws SQLException{
		super(con, tableName, legsTableName, ct, at, mt);
	}
	
	// AppListener support
	
	private AppListener al;
	
	public void addAppListener(AppListener l){
		al = (AppListener)AppEventMulticaster.add(al, l);
	}
	
	public void removeAppListener(AppListener l){
		al = (AppListener)AppEventMulticaster.remove(al, l);
	}
		
	public void assemble(Transactions tt) throws SQLException{
		this.clear();
		long l = 0;
		if(al != null) al.appProgress(new AppEvent(this, "Assembling settlement queue", 0));
		int psize = tt.settlementMatureSize();
		int pcount = 0;
		for(DBEnumeration e = tt.settlementMatureTransactions(); e.hasMoreElements();){
			Transaction t = (Transaction)e.nextElement();
			pcount++;
			if(al != null) al.appProgress(new AppEvent(this, "Processing of transaction " + pcount + " of " + psize, (pcount * 100) / psize));
			t.setStatus(Status.EXPIRED );
			t.save();
			t.setStatus(Status.SETTLEMENTMATURE );
			t.setParentReference(t.getReference());
			String ref = Transaction.makeReference("TR");
			t.setReference(ref);
			Transaction[] ts = t.split();
			for(int i = 0; i < ts.length; i++) this.save(ts[i]);
			Settlable s = (Settlable)t;
			getMovements().prBook(s, true);
			getMovements().paBook(t, false);
		}
		if(al != null) al.appProgress(new AppEvent(this, "Assembled " + pcount + " transactions", 0));
		setChanged();
	}
	
	public Bundle all() throws SQLException{
		Bundle b = new Bundle();
		for(DBEnumeration e = transactions(); e.hasMoreElements();){
			Transaction t = (Transaction)e.nextElement();
			b.addTransaction(t);
		}
		return b;
	}
	
	public int processSequentially(boolean failureManagement, TimeOfDay tod) throws SQLException{
		int queueSize = size();
		int settled = 0;
		if(al != null) al.appProgress(new AppEvent(this, "Settling settlement queue sequentially", 0));
		int psize = this.settlementMatureSize();
		int pcount = 0;
		for(DBEnumeration e1 = settlementMatureTransactions(); e1.hasMoreElements();){
			Settlable s = (Settlable)e1.nextElement();
			pcount++;
			if(al != null) al.appProgress(new AppEvent(this, "Processing for settlement transaction " + pcount + " of " + psize, (pcount * 100) / psize));
			Settlable sret = s.qualify(s.getSettlementAlgorithm(failureManagement), tod);
			if(sret != null){
				settled++;
				getMovements().book((Bookable)sret, tod);
				getMovements().prBook(sret, false);
				Transaction t = (Transaction)sret;
				t.setStatus("ST");
				t.save();
				if (sret != s){//
					Transaction t2 = (Transaction)s;//
					t2.setStatus("SP");//
					t2.save();//
					String myS = Transaction.getChild(t.getReference());
					Transaction t3 = (Transaction)t2.cloneDeep();
					t3.setReference(myS);
					t.inv();
					t3.addTransaction(t);
			        t3.setTable(t.getTable());
					t3.setStatus("SM");
					this.save(t3);
				}
			}
		}
		if(al != null) al.appProgress(new AppEvent(this, "Processed " + pcount + " transactions", 0));
		setChanged();
		return settled;
	}
	
	public int processCircles() throws SQLException{
		int result = 0;
		Bundle b1 = all();
		// result = b1.settleGlobally();
		System.out.println("Bundle: global settlement success rate=" + result + "/" + b1.size());
		for(Enumeration e1 = b1.clusters(); e1.hasMoreElements();){
			Bundle b2 = (Bundle)e1.nextElement();
			System.out.println("Bundle(cluster): " + b2);
			for(Enumeration e2 = b2.chainedGroups(3); e2.hasMoreElements();){
				Bundle b3 = (Bundle)e2.nextElement();
				System.out.println("Bundle(chained group): " + b3);
			}
		}
		setChanged();
		return 0;
	}
	
	public int rejectObsoleteTransactions(TimeOfDay tod) throws SQLException {
		int queueSize = size();
		int rejected = 0;
		boolean reject = false;
		if(al != null) al.appProgress(new AppEvent(this, "Rejecting obsolete transactions", 0));
		int psize = this.settlementMatureSize();
		int pcount = 0;
		for(DBEnumeration e1 = settlementMatureTransactions(); e1.hasMoreElements();){
			Transaction t = (Transaction)e1.nextElement();
			pcount++;
			if(al != null) al.appProgress(new AppEvent(this, "Checking for rejection transaction " + pcount + " of " + psize, (pcount * 100) / psize));
			reject = false;
			for(Iterator e2 = t.legs(); e2.hasNext() && !reject;){
				Leg l = (Leg)e2.next();
				if(tod.isAfter(l.getCurrency().getCC())) reject = true;
			}
			if(reject){
				Settlable s = (Settlable)t;
				getMovements().prBook(s, false);
				t.setStatus("RJ");
				t.save();
				rejected++;
			}	
		}
		if(al != null) al.appProgress(new AppEvent(this, "Rejected " + rejected + " transactions", 0));
		setChanged();
		return rejected;
	}
			
	public DBEnumeration bundles(String whereClause, int bundleSize) throws SQLException{
		return new QueueIterator(this, bundleSize);
	}
			
	public void printAll() throws SQLException{
		System.out.println("Queue:");
		Settlable q;
		for(DBEnumeration e = transactions(); e.hasMoreElements();){
			q = (Settlable)e.nextElement();
			System.out.println(q);
		}
	}
	
}