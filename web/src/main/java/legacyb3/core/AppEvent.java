package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import java.util.*;

public class AppEvent extends EventObject { 
	
	private String message;
	private String progressMessage;
	private int percentage; 
	private TimeOfDay now;
	
	public AppEvent(Object source){
		super(source);
	}
	
	public AppEvent(Object source, String message){
		super(source);
		this.message = message;
	}
	
	public AppEvent(Object source, String progressMessage, int percentage){
		super(source);
		this.progressMessage = progressMessage;
		this.percentage = percentage;
	}
	
	public AppEvent(Object source, TimeOfDay now){
		super(source);
		this.now = now;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String getProgressMessage(){
		return this.progressMessage;
	}
	
	public int getProgressPercentage(){
		return this.percentage;
	}
	
	public TimeOfDay getTimeOfDay(){
		return this.now;
	}
	
}
