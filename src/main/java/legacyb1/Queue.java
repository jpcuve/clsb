package legacyb1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class Queue extends Trs {
	private BalanceTypes btt;
	
	public Queue(Connection con, String tableName, String legsTableName, Currencies ct, Accounts at, BalanceTypes btt, Movements mt) throws SQLException{
		super(con, tableName, legsTableName, ct, at, mt);
		this.btt = btt;
	}
	
	public void assemble(Queue q) throws SQLException, DBIOException{
		q.clear();
		clearConstraints();
		getColumnDefinition("Status").setConstraintEqual("SM");
		long l = 0;
		for(Enumeration e = transactions(); e.hasMoreElements();){
			Tr t = (Tr)e.nextElement();
			t.setParentReference(t.getReference());
			long ti = System.currentTimeMillis();
			while(System.currentTimeMillis() - ti < 1); // you never know...
			String ref = "TR" + Long.toString(System.currentTimeMillis());
			t.setReference(ref);
			Tr[] ts = t.split();
			for(int i = 0; i < ts.length; i++) q.insert(ts[i]);
			Settlable s = (Settlable)t;
			getMovements().bookProjection(s, true);
		}
		setChanged();
		notifyObservers();
	}
	
	public Bundle all() throws SQLException{
		Bundle b = new Bundle();
		for(Enumeration e = transactions(); e.hasMoreElements();){
			Tr t = (Tr)e.nextElement();
			b.addTransaction(t);
		}
		return b;
	}
	
	public void processSequentially() throws SQLException, DBIOException{
		int queueSize = size();
		int settled = 0;
		getColumnDefinition("Status").setConstraintEqual("SM");
		for(Enumeration e1 = transactions(); e1.hasMoreElements();){
			Settlable s = (Settlable)e1.nextElement();
			Settlable sret = s.qualify(s.getAlgorithm());
			if(sret != null){
				settled++;
				getMovements().book(sret);
				getMovements().bookProjection(sret, false);
				Tr t = (Tr)s;
				t.setStatus("ST");
				t.save();
			}
		}
		System.out.println("Sequential settlement success rate=" + settled + "/" + queueSize);
		setChanged();
		notifyObservers();
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
		notifyObservers();
		return 0;
	}
		
	public Enumeration bundles(String whereClause, int bundleSize) throws SQLException{
		return new QueueIterator(this, bundleSize);
	}
		
	public void printAll() throws SQLException{
		System.out.println("Queue:");
		Settlable q;
		for(Enumeration e = transactions(); e.hasMoreElements();){
			q = (Settlable)e.nextElement();
			System.out.println(q);
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, DBIOException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		Balances bt = new Balances(con, at, btt, ct);
		MovementTypes mtt = new MovementTypes(con, att, btt);
		Movements mt = new Movements(con, mtt, ct, at);
		Queue q = new Queue(con, "SettlementQueue", "SettlementQueueLegs", ct, at, btt, mt);
		q.getColumnDefinition("Status").setConstraintEqual("SM");
		for(Enumeration e = q.transactions(); e.hasMoreElements();) System.out.println(e.nextElement());
	}
		
}