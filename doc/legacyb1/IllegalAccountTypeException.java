package legacyb1;

public class IllegalAccountTypeException extends Exception{
	public IllegalAccountTypeException(){
		super("Illegal account type.");
	}
	public IllegalAccountTypeException(String s){
		super("Illegal account type: " + s);
	}
}