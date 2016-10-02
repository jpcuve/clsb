package legacyb1;

import java.util.*;
import java.sql.*;

public class QueueIterator implements Enumeration{
	private Queue q;
	private int bundleSize;
	private Enumeration e;
	
	public QueueIterator(Queue q, int bundleSize) throws SQLException{
		this.q = q;
		this.bundleSize = bundleSize;
		this.e = q.transactions();
	}
	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement(){
		int i = bundleSize;
		Tr t = null;
		Bundle b = new Bundle();
		while(e.hasMoreElements() && i > 0){
			t = (Tr)e.nextElement();
			if (t.getStatus().toString().equals("SM")){
				b.addTransaction(t);
				i--;
			}
		}
		return b;
	}
}