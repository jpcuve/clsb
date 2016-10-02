package legacyb1;

import javax.swing.table.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class QueueTableModel extends AbstractTableModel{
	private Tr[] t;
	private int rows;
	private Class stringClass;
	
	public QueueTableModel(Queue q){
		try{
			stringClass = Class.forName("java.lang.String");
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		try{
			rows = q.size();
		}catch(SQLException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		t = new Tr[rows];
		int i = 0;
		for(Enumeration e = q.transactions(); e.hasMoreElements();){
			t[i] = (Tr)e.nextElement();
			i++;
		}
	}
	
	/*	
	public Class getColumnClass(int columnIndex){
		return stringClass;
	}
	*/
	
	public int getColumnCount(){
		return 6;
	}
	
	public String getColumnName(int columnIndex){
		switch(columnIndex){
			case 0: return "Ref";
			case 1: return "Status";
			case 2: return "Parent";
			case 3: return "Ac#1";
			case 4: return "Ac#2";
			case 5: return "Settl.am.";
		}
		return "";
	}
	
	public int getRowCount(){
		return rows;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex){
		NumberFormat nf = NumberFormat.getInstance();
		switch(columnIndex){
			case 0: return t[rowIndex].getReference();
			case 1: return t[rowIndex].getStatus();
			case 2: return t[rowIndex].getParentReference();
			case 3: return t[rowIndex].getFirstAccount().getName();
			case 4: return t[rowIndex].getSecondAccount().getName();
			case 5:
				nf.setMaximumFractionDigits(2);
				return nf.format(t[rowIndex].getApproximateSettlementAmount());
		}
		return "?";
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex){
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return false;
	}
	
	// miscellaneous
	
	public String toString(){
		return "Queue";
	}
	
	public Tr getTransaction(int index){
		return t[index];
	}
			
}
	