package legacyb1;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class BalancesTableModel extends DefaultTableModel{
	private String name;
	
	public BalancesTableModel(Currencies ct, BalanceTypes btt, Account a){
		addColumn("Ccy");
		for(Enumeration e1 = btt.balanceTypes(); e1.hasMoreElements();){
			BalanceType btyp = (BalanceType)e1.nextElement();
			addColumn(btyp.getName());
		}
		for(Enumeration e1 = ct.currencies(); e1.hasMoreElements(); ){
			Currency c = (Currency)e1.nextElement();
			int prec = c.getPrecision();
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(prec);
			nf.setMinimumFractionDigits(prec);
			Vector v = new Vector();
			v.addElement(c.getName());
			for(Enumeration e2 = btt.balanceTypes(); e2.hasMoreElements(); ){
				BalanceType btyp = (BalanceType)e2.nextElement();
				Balance b = a.getBalance(btyp, c);
				double bal = (b == null) ? 0 : b.getAmount();
				// System.out.println(bal);
				v.addElement(nf.format(bal));
			}
			addRow(v);
		}
		name = a.getName();
	}
	
	public String toString(){
		return name;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, DBIOException, IllegalAccountException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		Balances bt = new Balances(con, at, btt, ct);
		bt.fill();
		Account a = at.get("A");
		JFrame f = new JFrame("Balances");
		f.addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e) {System.exit(0);} });
		// AccountTypesTree tree = new AccountTypesTree(att);
		f.getContentPane().add(new JScrollPane(new JTable(new BalancesTableModel(ct, btt, a))));
		f.pack();
        f.setVisible(true);
	}
}