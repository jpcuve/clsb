package legacyb1;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Enumeration;

public class CurrencyGroupsTreeModel extends DefaultTreeModel{
	
	public CurrencyGroupsTreeModel(CurrencyGroups cgt, Currencies ct){
		super(new DefaultMutableTreeNode("Currencies"));
		DefaultMutableTreeNode top = (DefaultMutableTreeNode)getRoot();
		CurrenciesTableModel ctm = new CurrenciesTableModel(ct);
		top.setUserObject(ctm);
		for(Enumeration e1 = cgt.currencyGroups(); e1.hasMoreElements(); ){
			CurrencyGroup cg = (CurrencyGroup)e1.nextElement();
			DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(cg.getName());
			n1.setUserObject(new CurrenciesTableModel(cg));
			top.add(n1);
		}
		setRoot(top);
	}
}
	