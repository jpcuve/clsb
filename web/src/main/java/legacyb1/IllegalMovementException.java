package legacyb1;

public class IllegalMovementException extends Exception{
	public IllegalMovementException(){
		super("Illegal movement.");
	}
	public IllegalMovementException(String s){
		super("Illegal movement: " + s);
	}
}