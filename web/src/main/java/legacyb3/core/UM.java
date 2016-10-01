package legacyb3.core;

import legacyb3.util.*;

import java.util.HashMap;
import java.util.Iterator;

public class UM extends Party {
	
	protected HashMap umPositions= new HashMap();
	private SM dDSM;
	
	public UM(String id){
		super(id);
	}
	
	public UM(String id, String name){
		super(id, name, "UM");
	}
	
	public UM(String id, String name, SM dDSM){
		super(id, name, "UM");
		this.dDSM = dDSM;
	}
	
	public void setUMPos(UMPositionWithSM umPos){		
		String key = umPos.getSM().getID();
		if(this != umPos.getUM()) System.out.println("Assigning wrong UM pos");
		umPositions.put(key, umPos);
		setChanged();
		notifyObservers();
	}
	
	public UMPositionWithSM getUMPos(SM sm){
		String key = sm.getID();
		UMPositionWithSM umPos = (UMPositionWithSM)umPositions.get(key);
		/*
		if(umPos == null) {
			umPos = new UMPositionWithSM(sm,this,false,0,0,0,0);
			umPositions.put(key,umPos);
		}
		*/
		return umPos;
	}
	
	public void setDefaultDsm(SM sm){
		dDSM = sm;
	}
	
	public SM getDefaultDsm(){
		return this.dDSM;
	}
	
	public Iterator pos(){
		return umPositions.values().iterator();
	}
	
	public Object clone(){
		UM um = (UM)super.clone();
		um.umPositions = (HashMap)this.umPositions.clone();
		return um;
	}
}
