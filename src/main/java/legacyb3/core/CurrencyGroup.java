package legacyb3.core;

import java.util.Iterator;
import java.util.TreeMap;

public class CurrencyGroup extends ClsObject {
	private String id;
	private String name;
	private long priority;
	protected TreeMap ccys;
	protected TreeMap pids;
	
	// constructors
	
	public CurrencyGroup(String id){
		this(id, null, 0);
	}
	
	public CurrencyGroup(String id, String name, long priority){
		setID(id);
		setName(name);
		setPriority(priority);
		this.ccys = new TreeMap();
		this.pids = new TreeMap();
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
	
	public void setPriority(long priority){
		this.priority = priority;
	}
	
	public long getPriority(){
		return priority;
	}
	/*
	public void setCurrency(Currency c){
		currencies.addElement(c);
	}
	
	public Enumeration currencies(){
		return currencies.elements();
	}
	
	public void setPayInDeadline(PayInDeadline pid){
		pids.put(pid.getID(), pid);
	}
	
	public PayInDeadline getPayInDeadline(String id){
		return (PayInDeadline)pids.get(id);
	}
	
	public Enumeration payInDeadlines(){
		return this.pids.elements();
	}
	*/
	
	public void addCurrency(Currency ccy){
		this.ccys.put(ccy.getID(), ccy);
	}
	
	public void addPayInDeadline(PayInDeadline pid){
		this.pids.put(new Integer(pid.getTimeOfDay().getMinutes()), pid);
	}
	
	public Iterator currencies(){
		return this.ccys.values().iterator();
	}
	
	public Iterator payInDeadlines(){
		return this.pids.values().iterator();
	}
	
	// JAVA support
	
	public String toString(){
		return "[group=" + name + ", priority=" + priority + "]";
	}
	
	public Object clone(){ // shallow
		CurrencyGroup cg = (CurrencyGroup)super.clone();
		cg.ccys = (TreeMap)this.ccys.clone();
		cg.pids = (TreeMap)this.pids.clone();
		return cg;
	}
	
	public Object cloneDeep(){
		CurrencyGroup cg = (CurrencyGroup)this.clone();
		for(Iterator i = this.currencies(); i.hasNext();){
			Currency c = (Currency)i.next();
			cg.addCurrency((Currency)c.clone());
		}
		for(Iterator i = this.payInDeadlines(); i.hasNext();){
			PayInDeadline pid = (PayInDeadline)i.next();
			cg.addPayInDeadline((PayInDeadline)pid.clone());
		}
		return cg;
	}
	
	public int size(){
		return ccys.size();
	}
	
	public int hashCode(){
		return id.hashCode();
	}

}