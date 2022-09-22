package legacyb1;

public class IllegalBalanceTypeException extends Exception{
	public IllegalBalanceTypeException(){
		super("Illegal balance type.");
	}
	public IllegalBalanceTypeException(String s){
		super("Illegal balance type: " + s);
	}
}