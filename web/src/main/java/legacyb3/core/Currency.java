package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import legacyb3.data.*;
import legacyb3.util.*;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

public class Currency extends ClsObject implements Cloneable{
	private String name;
	private boolean neligible;
	private double baseRate;
	private double volatilityMargin;
	private boolean quote;
	private double gst;
	private int precision;
	private double unit;
	private CurrencyGroup cg;
	private double balFact;
	private Account mirror;
	private TimeOfDay RTGSOpen;
	private TimeOfDay RTGSClose;
	private TimeOfDay fctt;
	private TimeOfDay cc;
	private double minPI;
	private double minPO;
	private double maxPO;
	private boolean suspendedForPayOut;
	
	protected Vector rtgsco;
	protected TreeMap pips;
	
	// constructors

	public Currency(String name, 
					boolean eligible,
					double baseRate, 
					boolean quote, 
					double volatilityMargin, 
					double gst, 
					int precision, 
					double unit,
					CurrencyGroup cg, 
					double balFact,
					TimeOfDay rtgsOpen,
					TimeOfDay rtgsClose,
					TimeOfDay fctt,
					TimeOfDay cc,
					double minPI,
					double minPO,
					double maxPO,
					boolean suspendedForPayOut){
		setID(name);
		if (neligible) setNotEligible(); else clearNotEligible();
		setBaseRate(baseRate);
		setVolatilityMargin(volatilityMargin);
		setGrossSplitThreshold(gst);
		setUnit(unit);
		setPrecision(precision);
		setCurrencyGroup(cg);
		setBalanceFactor(balFact);
		setMirrorAccount(null);
		setRTGSOpen(rtgsOpen);
		setRTGSClose(rtgsClose);
		setFCTT(fctt);
		setCC(cc);
		setMinimumPayIn(minPI);
		setMinimumPayOut(minPO);
		setMaximumPayOut(maxPO);
		if (suspendedForPayOut) setSuspendedForPayOut(); else clearSuspendedForPayOut();
		rtgsco = new Vector();
		pips = new TreeMap();
	}
	
	public Currency(String name){
		this(name, false, 0, false, 0, 0, 0, 0, null, 0, null, null, null, null, 0, 0, 0, false);
	}
	
	// accessors

	public void setID(String name){
		this.name = name;
	}
	
	public String getID(){
		return name;
	}
	
	public void setNotEligible(){
		this.neligible = true;
	}
	
	public void clearNotEligible(){
		this.neligible = false;
	}
	
	public boolean isNotEligible(){
		return neligible;
	}
	
	public boolean isEligible(){
		return !this.neligible;
	}
	
	public void setBaseRate(double baseRate){
		this.baseRate = baseRate;
	}
	
	public double getBaseRate(){
		return baseRate;
	}
	
	public void setQuote(){
		this.quote = true;
	}
	
	public void clearQuote(){
		this.quote = false;
	}
	
	public boolean isQuote(){
		return this.quote;
	}
	
	public void setVolatilityMargin(double volatilityMargin){
		this.volatilityMargin = volatilityMargin;
	}
	
	public double getVolatilityMargin(){
		return volatilityMargin;
	}
	
	public void setGrossSplitThreshold(double gst){
		this.gst = gst;
	}
	
	public double getGrossSplitThreshold(){
		return gst;
	}
	
	public void setPrecision(int p){
		this.precision = p;
	}
	
	public int getPrecision(){
		return precision;
	}
	
	public void setUnit(double unit){
		this.unit = unit;
	}
	
	public double getUnit(){
		return this.unit;
	}
	
	public void setCurrencyGroup(CurrencyGroup cg){
		this.cg = cg;
	}
	
	public CurrencyGroup getCurrencyGroup(){
		return cg;
	}
	
	public void setBalanceFactor(double balFact){
		this.balFact = balFact;
	}
	
	public double getBalanceFactor(){
		return balFact;
	}
	
	public void setMirrorAccount(Account mirror){
		this.mirror = mirror;
	}
	
	public Account getMirrorAccount(){
		return this.mirror;
	}
	
	public void setRTGSOpen(TimeOfDay tod){
		this.RTGSOpen = tod;
	}
	
	public TimeOfDay getRTGSOpen(){
		return this.RTGSOpen;
	}
	
	public void setRTGSClose(TimeOfDay tod){
		this.RTGSClose = tod;
	}
	
	public TimeOfDay getRTGSClose(){
		return this.RTGSClose;
	}
	
	public void setFCTT(TimeOfDay tod){
		this.fctt = tod;
	}
	
	public TimeOfDay getFCTT(){
		return this.fctt;
	}
	
	public void setCC(TimeOfDay tod){
		this.cc = tod;
	}
	
	public TimeOfDay getCC(){
		return this.cc;
	}
	
	public void setMinimumPayIn(double minPI){
		this.minPI = minPI;
	}
	
	public double getMinimumPayIn(){
		return this.minPI;
	}
	
	public void setMinimumPayOut(double minPO){
		this.minPO = minPO;
	}
	
	public double getMinimumPayOut(){
		return minPO;
	}
	
	public void setMaximumPayOut(double maxPO){
		this.maxPO = maxPO;
	}
	
	public double getMaximumPayOut(){
		return maxPO;
	}
	
	public void setSuspendedForPayOut(){
		this.suspendedForPayOut = true;
	}
	
	public void clearSuspendedForPayOut(){
		this.suspendedForPayOut = false;
	}
	
	public boolean isSuspendedForPayOut(){
		return suspendedForPayOut;
	}
	
	public void addRTGSCutOff(RTGSCutOff co){
		rtgsco.addElement(co);
	}
	
	public Enumeration rtgsCutOffs(){
		return rtgsco.elements();
	}
	
	public boolean isRTGSCutOff(TimeOfDay now){
		boolean ret = false;
		for(Enumeration e1 = rtgsCutOffs(); e1.hasMoreElements() && !ret;){
			RTGSCutOff co = (RTGSCutOff)e1.nextElement();
			ret = now.isAfterOrEqual(co.getStart()) && now.isBeforeOrEqual(co.getEnd());
		}
		return ret;
	}
	
	public void setPayInPercentage(PayInPercentage pip){
		TimeOfDay tod = pip.getTimeOfDay();
		pips.put(new Integer(tod.getMinutes()), pip);
	}
	
	public Iterator payInTimes(){
		return this.payInTimes(true);
	}
	
	public Iterator payInTimes(boolean dir){
		TreeMap tm = new TreeMap();
		for(Iterator i1 = this.pips.values().iterator(); i1.hasNext();){
			TimeOfDay tod = ((PayInPercentage)i1.next()).getTimeOfDay();
			tm.put(new Integer(tod.getMinutes()), tod);
		}
		return tm.values().iterator();
	}
	
	public double getPercentage(TimeOfDay tod){
		PayInPercentage pip = (PayInPercentage)pips.get(new Integer(tod.getMinutes()));
		if(pip == null) return 0; // should not happen...
		return pip.getPercentage();
	}
	
	// JAVA support
	
	public String toString(){
		return "[" + name + ", eligible=" + isEligible() + ", rate=" + getRate() + ", vm=" + volatilityMargin 
			+ ", gst=" + gst + ", prec=" + precision + ", unit=" + unit + ", group=" + cg.getID() 
			+ ", mirror=" + mirror.getID() + ", bal.fact=" + balFact // + ", abspr=" + getAbsolutePriority()
			+ ", minPI=" + minPI + ", minPO=" + minPO + ", maxPO=" + maxPO + "] ";
	}
	
	public int hashCode(){
		return name.hashCode();
	}

	public Object clone(){
		Currency c = (Currency)super.clone();
		c.rtgsco = (Vector)this.rtgsco.clone();
		c.pips = (TreeMap)this.pips.clone();
		return c;
	}
	
	public Object cloneDeep(){
		Currency c = (Currency)this.clone();
		for(Enumeration e1 = this.rtgsCutOffs(); e1.hasMoreElements();){
			RTGSCutOff co = (RTGSCutOff)e1.nextElement();
			c.addRTGSCutOff((RTGSCutOff)co.clone());
		}
		// missing pips clone!
		return c;
	}

	public boolean equals(Object o){
		Currency c = (Currency)o;
		return c.getID().equals(name);
	}
	
	// miscellaneous
	
	public double getRate(){
		return (quote) ? baseRate : (1 / baseRate);
	}
	
	public double zero(){
		return Math.pow(10, -precision - 2);
	}
	
	public double round(double am){
		double fact = Math.pow(10, precision);
		return Math.round(am * fact) / fact;
	}
	
	public double floorUnit(double am){
		return Math.floor(this.round(am) / unit) * unit;
	}
	
	public double ceilUnit(double am){
		return Math.ceil(this.round(am) / unit) * unit;
	}
	
	public double roundDown(double am){
		double fact2 = Math.pow(10, precision + 2);
		double d = Math.round(am * fact2) / fact2;
		double fact = Math.pow(10, precision);
		return Math.floor(d * fact) / fact;
	}
	
	public double roundUp(double am){
		double fact2 = Math.pow(10, precision + 2);
		double d = Math.round(am * fact2) / fact2;
		double fact = Math.pow(10, precision);
		return Math.ceil(d * fact) / fact;
	}
	
	public double getWeightedBalance(){
		double bf = getBalanceFactor();
		return (bf == 0) ? 0 : -mirror.getCurrentBalance(this) * getRate() / bf;
	}
	
}