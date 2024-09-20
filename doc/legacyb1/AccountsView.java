package legacyb1;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class AccountsView extends JSplitPane implements TreeSelectionListener{
	private AccountTypes att;
	private Accounts at;
	private Currencies ct;
	private BalanceTypes btt;
	
	public AccountsView(AccountTypes att, Accounts at, Currencies ct, BalanceTypes btt){
		this.att = att;
		this.at = at;
		this.ct = ct;
		this.btt = btt;
		
		JTree tree = new JTree(new AccountTypesTreeModel(att, at, btt, ct));
		tree.addTreeSelectionListener(this);
		JScrollPane dtree = new JScrollPane(tree);
		dtree.setMinimumSize(new Dimension(100,100));
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setOneTouchExpandable(true);
		setContinuousLayout(false);
		setLeftComponent(dtree);
		tree.setSelectionRow(0);
	}
	
	public void valueChanged(TreeSelectionEvent e){
		TreePath tp = e.getNewLeadSelectionPath();
		int level = tp.getPathCount();
		DefaultMutableTreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
		DefaultTableModel tm = (DefaultTableModel)tn.getUserObject();
		// System.out.println(tp + ", level=" + level + ", object=" + tm);
		JTable table = new JTable(tm);
		JScrollPane dtable = new JScrollPane(table);
		dtable.setMinimumSize(new Dimension(100, 100));
		setRightComponent(dtable);
	}
	
/*
	public static void main(String[] args) throws ClassNotFoundException, SQLException, DBIOException {
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
		for(Enumeration e1 = att.accountTypes(); e1.hasMoreElements();){
			AccountType actyp = (AccountType)e1.nextElement();
			for(Enumeration e2 = actyp.accounts(); e2.hasMoreElements();){
				Account a = (Account)e2.nextElement();
			}
		}
			
		JFrame f = new JFrame("Accounts");
		f.addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e) {System.exit(0);} });
		f.getContentPane().add(new AccountsView(att, at, ct, btt));
		f.pack();
		f.setVisible(true);
	}
*/
}