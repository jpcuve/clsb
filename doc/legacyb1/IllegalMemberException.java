package legacyb1;

public class IllegalMemberException extends Exception{
	public IllegalMemberException(){
		super("Illegal member.");
	}
	public IllegalMemberException(String s){
		super("Illegal member: " + s);
	}
}