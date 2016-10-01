package legacyb3.core;

import legacyb3.util.*;
import legacyb3.data.*;

public class Party extends ClsObject {
	private String id;
	private String name;
	private String type;
	
	// constructors
	
	public Party(String id){
		this(id, null, null);
	}
	
	public Party(String id, String name, String type){
		setID(id);
		setName(name);
		setPartyType(type);
	}
	
	// accessors
	
	public void setID(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPartyType(String patyp){
		this.type = patyp;
	}
	
	public String getPartyType(){
		return this.type;
	}
	
	// JAVA support
	
	public String toString(){
		return "[id=" + id + ", name=" + name + "]";
	}
	
	public Object clone(){ // shallow
		Party pt = (Party)super.clone();
		return pt;
	}
		
	public int hashCode(){
		return id.hashCode();
	}

}