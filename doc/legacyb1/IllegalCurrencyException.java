package legacyb1;

public class IllegalCurrencyException extends Exception{
	public IllegalCurrencyException(){
		super("Illegal currency.");
	}
	public IllegalCurrencyException(String s){
		super(s);
	}
}