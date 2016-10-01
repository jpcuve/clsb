package legacyb3.data;

import legacyb3.or2.*;
import legacyb3.core.*;

public class TransactionFK extends DBField {
	private static TransactionFK TRANSACTION = null;
	
	public static TransactionFK instance(){
		if(TRANSACTION == null) TRANSACTION = new TransactionFK();
		return TRANSACTION;
	}
	
	private TransactionFK(){
		super(Transaction.class);
	}
	
	public Object toUser(Object o){
		return null;
	}
	
	public Object toJDBC(Object o){
		return ((Transaction)o).getReference();
	}
	
	public String toSQL(Object o){
		return "'" + ((Transaction)o).getReference() + "'";
	}

}
