package legacyb3.core;

import legacyb3.or2.DBRecord;

public class ClsObject extends DBRecord { 

	public DBRecord duplicate(){
		return (DBRecord)this.clone();
	}
	
	public static String makeReference(String s){
		long t = System.currentTimeMillis();
		while(System.currentTimeMillis() - t < 1); // you never know...
		return s + Long.toString(System.currentTimeMillis());
	}

}
