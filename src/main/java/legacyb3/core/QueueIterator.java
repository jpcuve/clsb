package legacyb3.core;

import legacyb3.data.Queue;
import legacyb3.or2.DBEnumeration;

import java.sql.SQLException;

public class QueueIterator implements DBEnumeration{
	private Queue q;
	private int bundleSize;
	private DBEnumeration e;
	
	public QueueIterator(Queue q, int bundleSize) throws SQLException{
		this.q = q;
		this.bundleSize = bundleSize;
		this.e = q.transactions();
	}
	
	public boolean hasMoreElements(){
		return e.hasMoreElements();
	}
	
	public Object nextElement() throws SQLException{
		int i = bundleSize;
		Transaction t = null;
		Bundle b = new Bundle();
		while(e.hasMoreElements() && i > 0){
			t = (Transaction)e.nextElement();
			if (t.getStatus().toString().equals(Status.SETTLEMENTMATURE)){
				b.addTransaction(t);
				i--;
			}
		}
		return b;
	}
}