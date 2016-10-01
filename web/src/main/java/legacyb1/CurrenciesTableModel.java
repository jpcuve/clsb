package legacyb1;

import javax.swing.table.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class CurrenciesTableModel extends AbstractTableModel{
	private String name;
	private Currency[] c;
	private int rows;
	private Class stringClass;
		
	public CurrenciesTableModel(Currencies ct){
		try{
			stringClass = Class.forName("java.lang.String");
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		try{
			rows = ct.size();
		}catch(SQLException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		c = new Currency[rows];
		int i = 0;
		for(Enumeration e = ct.currencies(); e.hasMoreElements();){
			c[i] = (Currency)e.nextElement();
			i++;
		}
		name = "Currencies";
	}
	
	public CurrenciesTableModel(CurrencyGroup cg){
		try{
			stringClass = Class.forName("java.lang.String");
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		rows = cg.size();
		c = new Currency[rows];
		int i = 0;
		for(Enumeration e = cg.currencies(); e.hasMoreElements();){
			c[i] = (Currency)e.nextElement();
			i++;
		}
		name = cg.getName();
	}
	
	public int getColumnCount(){
		return 7;
	}

	public String getColumnName(int columnIndex){
		switch(columnIndex){
			case 0: return "ID";
			case 1: return "Rate";
			case 2: return "v.m.";
			case 3: return "Gross Split Threshold";
			case 4: return "Precision";
			case 5: return "Balance factor";
			case 6: return "CB mirror";
		}
		return "";
	}
	
	public int getRowCount(){
		return rows;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex){
		NumberFormat nf = NumberFormat.getInstance();
		NumberFormat pf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(5);
		pf.setMaximumFractionDigits(2);
		switch(columnIndex){
			case 0: return c[rowIndex].getName();
			case 1: return nf.format(c[rowIndex].getBaseRate()) + (c[rowIndex].getQuote() ? "*" : "");
			case 2: return pf.format(c[rowIndex].getVolatilityMargin());
			case 3: return nf.format(c[rowIndex].getGrossSplitThreshold());
			case 4: return nf.format(c[rowIndex].getPrecision());
			case 5: return nf.format(c[rowIndex].getBalanceFactor());
			case 6: return c[rowIndex].getMirrorAccount().getName();
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
		return name;
	}
	
}
			