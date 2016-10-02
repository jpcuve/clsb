package legacyb3.core;

import java.sql.*;

public class TooManyLegsException extends SQLException{
	public TooManyLegsException(){
		super("Too many legs.");
	}
	public TooManyLegsException(String s){
		super("Too many legs: " + s);
	}

}