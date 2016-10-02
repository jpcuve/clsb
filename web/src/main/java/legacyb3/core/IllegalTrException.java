package legacyb3.core;

import java.sql.*;

public class IllegalTrException extends SQLException{
	public IllegalTrException(){
		super("Illegal transaction.");
	}
	public IllegalTrException(String s){
		super(s);
	}

}
