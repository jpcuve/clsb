package legacyb1;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CurrenciesView extends JSplitPane implements TreeSelectionListener{
	private CurrencyGroups cgt;
	private Currencies ct;
	
	public CurrenciesView(CurrencyGroups cgt, Currencies ct){
		this.cgt = cgt;
		this.ct = ct;
		
		JTree tree = new JTree(new CurrencyGroupsTreeModel(cgt, ct));
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
		CurrenciesTableModel tm = (CurrenciesTableModel)tn.getUserObject();
		// System.out.println(tp + ", level=" + level + ", object=" + tm);
		JTable table = new JTable(tm);
		JScrollPane dtable = new JScrollPane(table);
		dtable.setMinimumSize(new Dimension(100, 100));
		setRightComponent(dtable);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, DBIOException {
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		ct.fill();
			
		JFrame f = new JFrame("Currencies");
		f.addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e) {System.exit(0);} });
		f.getContentPane().add(new CurrenciesView(cgt, ct));
		f.pack();
        f.setVisible(true);
	}
}