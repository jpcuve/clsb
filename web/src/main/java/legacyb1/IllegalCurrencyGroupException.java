package legacyb1;

public class IllegalCurrencyGroupException extends Exception{
	public IllegalCurrencyGroupException(){
		super("Illegal currency group.");
	}
	public IllegalCurrencyGroupException(String s){
		super("Illegal currency group: " + s);
	}
}