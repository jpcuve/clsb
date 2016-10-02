package legacyb1;

import javax.swing.tree.*;
import java.util.*;

public class AccountTypesTreeModel extends DefaultTreeModel{
	
	
	public AccountTypesTreeModel(AccountTypes att, Accounts at, BalanceTypes btt, Currencies ct){
		super(new DefaultMutableTreeNode("Accounts"));
		DefaultMutableTreeNode top = (DefaultMutableTreeNode)getRoot();
		top.setUserObject(new AccountsTableModel(at));
		for(Enumeration e1 = att.accountTypes(); e1.hasMoreElements(); ){
			AccountType actyp = (AccountType)e1.nextElement();
			Enumeration e2 = actyp.accounts();
			if(e2.hasMoreElements()){
				DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(actyp.getID());
				n1.setUserObject(new AccountsTableModel(actyp));
				top.add(n1);
				while(e2.hasMoreElements()){
					Account a = (Account)e2.nextElement();
					DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(a.getName());
					n2.setUserObject(new BalancesTableModel(ct, btt, a));
					n1.add(n2);
				}
			}
		}
		setRoot(top);
	}
	
}

		
		