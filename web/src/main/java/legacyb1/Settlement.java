package legacyb1;

import java.sql.*;

public class Settlement{
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, DBIOException{
		if(args.length < 2){
			System.out.println("Error: please specify ODBC DB name & action");
			System.exit(1);
		}
		Class.forName ("com.ms.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:" + args[0]);
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		Balances bt = new Balances(con, at, btt, ct);
		MovementTypes mtt = new MovementTypes(con, att, btt);
		Movements mt = new Movements(con, mtt, ct, at);
		if(args[1].equals("assemble")){
			mt.clear();
			bt.load();
			Queue qi = new Queue(con, "Trs", "TrLegs", ct, at, btt, mt);
			Queue q = new Queue(con, "SettlementQueue", "SettlementQueueLegs", ct, at, btt, mt);
			// qi.printAll();
			System.out.println("Assembling settlement queue");
			qi.assemble(q);
			System.out.println("Settlement queue size=" + q.size());
			bt.save();
			// q.printAll();
		  // at.printAll();
		}
		if(args[1].equals("once")){
			bt.load();
			at.printAll();
			Queue q = new Queue(con, "SettlementQueue", "SettlementQueueLegs", ct, at, btt, mt);
			System.out.println("Processing...");
			q.processSequentially();
			// q.processCircles();
			System.out.println("Processed.");
			bt.save();
			at.printAll();
		}
	}
}