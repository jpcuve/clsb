package legacyb1;

public class TooManyLegsException extends Exception{
	public TooManyLegsException(){
		super("Too many legs.");
	}
	public TooManyLegsException(String s){
		super("Too many legs: " + s);
	}

}