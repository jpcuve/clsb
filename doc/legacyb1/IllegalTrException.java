package legacyb1;

public class IllegalTrException extends Exception{
	public IllegalTrException(){
		super("Illegal transaction.");
	}
	public IllegalTrException(String s){
		super(s);
	}

}
