package legacyb3.core;
import legacyb3.util.*;

public class MatchingInputComparator implements BinaryComparator { 
	
	public int compare(Object o1, Object o2){
		MatchingInput mInp1 = (MatchingInput)o1;
		String s1 = mInp1.getReference();
		MatchingInput mInp2 = (MatchingInput)o2;
		String s2 = mInp2.getReference();
		return s1.compareTo(s2);
	}
	
	public long evaluate(Object o, Object compareValue){
		MatchingInput mInp = (MatchingInput)o;
		String s = (String)compareValue;
		return (long)mInp.getReference().compareTo(s);
	}

}