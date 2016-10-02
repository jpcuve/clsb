package legacyb3.core;

import legacyb3.util.*;
import legacyb3.data.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;

public class MatchingInput extends ClsObject {
	private String matchingRef;
	private Party orig;
	private SM dsmOrig;
	private Party counterp;
	private SM dsmCounterp;
	private HashMap matchedInputs;
	
	public MatchingInput(String ref){
		matchingRef = ref;
		matchedInputs =	new HashMap();
	}
	// Readable , Writable
	public void setMatchingRef(String ref){
		matchingRef = ref;
	}
	public String getMatchingRef(){
		return matchingRef;
	}
	public void setOrig(Party pt){
		orig = pt;
	}
	public Party getOrig(){
		return orig;
	}
	public void setDsmOrig(SM dsm){
		dsmOrig = dsm;
	}
	public SM getDsmOrig(){
		return dsmOrig;
	}
	public void setCounterp(Party pt){
		counterp = pt;
	}
	public Party getCounterp(){
		return counterp;
	}
	public void setDsmCounterp(SM dsm){
		dsmCounterp = dsm;
	}
	public SM getDsmCounterp(){
		return dsmCounterp;
	}
	public void setMatchedInput(Input inp){
		matchedInputs.put(inp.getOriginator().getID(),inp);
	}
	public Input getMatchedInput(String origName){
		return (Input)matchedInputs.get(origName);
	}
	// Interface implement
	public void setReference(String ref){
		setMatchingRef(ref);
	}	
	public String getReference(){
		return getMatchingRef();
	}
	public void setStatus(String st){
		for(Iterator e = matchedInputs.values().iterator(); e.hasNext();){
			Input inp = (Input)e.next();
			inp.setStatus(st);
			}
	}
	public String getStatus(){
		String st1 = null;
		String st2 = null;
		for(Iterator e = matchedInputs.values().iterator(); e.hasNext();){
			Input inp = (Input)e.next();
			st1 = inp.getStatus();
			if((st2 != null) && !(st2.equals(st1))){
				System.out.println("Inputs contained in InputMatched have no the same status");
				return null;}
			st2 = st1;
			}
		return st2;
	}
	public Object clone(){
		MatchingInput mInp = (MatchingInput)super.clone();
		mInp.matchedInputs = (HashMap)this.matchedInputs.clone();
		return mInp;
	}
	public void SaveInputs() throws SQLException{
		for(Iterator e = matchedInputs.values().iterator(); e.hasNext();){
			Input i = (Input)e.next();
			i.save();
		}
	}
	// Methods
	public boolean contains(Object o){
		return matchedInputs.values().contains(o);
	}
	// Java support
	public String toString(){
		return "[ref : " + matchingRef
				    + " ordId : " + orig.getID()
					+ " dsmId : " + dsmOrig.getID()
					+ " countId : " + counterp.getID()
					+ " countDsmdId : " + dsmCounterp.getID()
				   + " status : " + getStatus();
	}
}

