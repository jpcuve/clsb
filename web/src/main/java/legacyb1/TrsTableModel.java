package legacyb1;

import javax.swing.table.*;
import java.util.*;
import java.text.*;

public class TrsTableModel extends DefaultTableModel{
	private String name;
	
	public TrsTableModel(Queue q){
		createColumns();
		for(Enumeration e1 = q.transactions(); e1.hasMoreElements(); ){
			Tr t = (Tr)e1.nextElement();
			createRow(t);
		}
		name = "Transactions";
	}
	
	private void createColumns(){
		addColumn("Ref");
		addColumn("Status");
		addColumn("Parent");
		addColumn("Ac#1");
		addColumn("Ac#2");
		addColumn("Settl.am.");
		addColumn("Buy ccy");
		addColumn("Buy amount");
		addColumn("Sell ccy");
		addColumn("Sell amount");
	}
	
	private void createRow(Tr t){
		NumberFormat nf = NumberFormat.getInstance();
		Vector v = new Vector();
		v.addElement(t.getReference());
		v.addElement(t.getStatus());
		v.addElement(t.getParentReference());
		v.addElement(t.getFirstAccount().getName());
		v.addElement(t.getSecondAccount().getName());
		nf.setMaximumFractionDigits(2);
		v.addElement(nf.format(t.getApproximateSettlementAmount()));
		int vsize = v.size();
		for(int i = 0; i < 4; i++) v.addElement("-");
		if(t.getClass().getName().equals("GrossTransaction")){
			for(Enumeration e1 = t.legs(); e1.hasMoreElements();){
				Leg l = (Leg)e1.nextElement();
				Currency ccy = l.getCurrency();
				int i = l.getDir() ? 2 : 0;
				v.setElementAt(ccy.getName(), vsize + i);
				int precision = ccy.getPrecision();
				nf.setMaximumFractionDigits(precision);
				nf.setMinimumFractionDigits(precision);
				v.setElementAt(nf.format(l.getAmount()), vsize + i + 1);
			}
		}
		addRow(v);
	}
	
	public String toString(){
		return name;
	}
			
}
	
	
	