package legacyb1;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

public class AccountTypesTree extends JPanel{
	
	public AccountTypesTree(AccountTypes att){
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Accounts");
		for(Enumeration e1 = att.accountTypes(); e1.hasMoreElements(); ){
			AccountType actyp = (AccountType)e1.nextElement();
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(actyp.getName());
			top.add(n);
			for(Enumeration e2 = actyp.accounts(); e2.hasMoreElements(); ){
				Account a = (Account)e2.nextElement();
				n.add(new DefaultMutableTreeNode(a.getName()));
			}
		}
		JTree tree = new JTree(top);
		tree.setRootVisible(false);
		setLayout(new BorderLayout());
		add(new JScrollPane(tree), BorderLayout.CENTER);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, DBIOException{
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		at.fill();
		Frame f = new Frame("Account types");
		f.addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e) {System.exit(0);} });
		AccountTypesTree tree = new AccountTypesTree(att);
		f.add(new JScrollPane(tree));
		f.pack();
        f.setVisible(true);
	}
}

		
		