package legacyb1;

import javax.swing.table.*;
import java.util.*;
import java.text.*;

public class AccountsTableModel extends DefaultTableModel{
	private String name;
	
	public AccountsTableModel(Accounts at){
		createColumns();
		for(Enumeration e1 = at.accounts(); e1.hasMoreElements(); ){
			Account a = (Account)e1.nextElement();
			createRow(a);
		}
		name = "Accounts";
	}
	
	public AccountsTableModel(AccountType actyp){
		createColumns();
		for(Enumeration e1 = actyp.accounts(); e1.hasMoreElements(); ){
			Account a = (Account)e1.nextElement();
			createRow(a);
		}
		name = actyp.getName();
	}
	
	private void createColumns(){
		addColumn("ID");
		addColumn("ASPL");
		addColumn("NC collateral");
		addColumn("Position");
		addColumn("Position w/ v.m.");
		addColumn("Short");
		addColumn("Long");
	}
	
	private void createRow(Account a){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		Vector v = new Vector();
		v.addElement(a.getName());
		v.addElement(nf.format(a.getASPL()));
		v.addElement(nf.format(a.getNonCashCollateral()));
		v.addElement(nf.format(a.getPosition("Net", false)));
		v.addElement(nf.format(a.getPosition("Net", true)));
		v.addElement(nf.format(a.getPosition("Short", false)));
		v.addElement(nf.format(a.getPosition("Long", false)));
		addRow(v);
	}
	
	public String toString(){
		return name;
	}
			
}