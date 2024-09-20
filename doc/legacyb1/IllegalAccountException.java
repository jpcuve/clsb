package legacyb1;

public class IllegalAccountException extends Exception{
	public IllegalAccountException(){
		super("Illegal account.");
	}
	public IllegalAccountException(String s){
		super("Illegal account: " + s);
	}
}