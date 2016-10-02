package legacyb1;

public class IllegalMovementTypeException extends Exception{
	public IllegalMovementTypeException(){
		super("Illegal movement type.");
	}
	public IllegalMovementTypeException(String s){
		super("Illegal movement type: " + s);
	}
}