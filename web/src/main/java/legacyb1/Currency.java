package legacyb1;

public class Currency extends DBRecord{
	private String name;
	private double baseRate;
	private double volatilityMargin;
	private boolean quote;
	private double gst;
	private int precision;
	private CurrencyGroup cg;
	private double balFact;
	private Account mirror;
	private TimeOfDay RTGSOpen;
	private TimeOfDay RTGSClose;
	private TimeOfDay fctt;
	private TimeOfDay cc;
	private double minPO;
	private double maxPO;
	
	// constructors

	public Currency(String name, 
									double baseRate, 
									boolean quote, 
									double volatilityMargin, 
									double gst, 
									int precision, 
									CurrencyGroup cg, 
									double balFact,
									TimeOfDay rtgsOpen,
									TimeOfDay rtgsClose,
									TimeOfDay fctt,
									TimeOfDay cc,
									double minPO,
									double maxPO){
		setName(name);
		setBaseRate(baseRate);
		setVolatilityMargin(volatilityMargin);
		setGrossSplitThreshold(gst);
		setPrecision(precision);
		setCurrencyGroup(cg);
		setBalanceFactor(balFact);
		setMirrorAccount(null);
		setRTGSOpen(rtgsOpen);
		setRTGSClose(rtgsClose);
		setFCTT(fctt);
		setCC(cc);
		setMinimumPayOut(minPO);
		setMaximumPayOut(maxPO);
	}
	
	public Currency(String name){
		this(name, 0, false, 0, 0, 0, null, 0, null, null, null, null, 0, 0);
	}
	
	// accessors

	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setBaseRate(double baseRate){
		this.baseRate = baseRate;
	}
	
	public double getBaseRate(){
		return baseRate;
	}
	
	public void setQuote(boolean q){
		this.quote = q;
	}
	
	public boolean getQuote(){
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
	
	// JAVA support
	
	public String toString(){
		return "[" + name + ", rate=" + getRate() + ", vm=" + volatilityMargin 
			+ ", gst=" + gst + ", prec=" + precision + ", group=" + cg.getName() 
			+ ", mirror=" + mirror.getName() + ", bal.fact=" + balFact // + ", abspr=" + getAbsolutePriority()
			+ ", minPO=" + minPO + ", maxPO=" + maxPO + "] ";
	}
	
	public int hashCode(){
		return name.hashCode();
	}

	public Object clone(){
		return new Currency(name, baseRate, quote, volatilityMargin, gst, precision, cg, balFact, RTGSOpen, RTGSClose, fctt, cc, minPO, maxPO);
	}
	
	public boolean equals(Object o){
		Currency c = (Currency)o;
		return c.getName().equals(name);
	}
	
	// miscellaneous
	
	public double getRate(){
		return (quote) ? baseRate : (1 / baseRate);
	}
	
	public double round(double am){
		double fact = Math.pow(10, precision);
		return Math.round(am * fact) / fact;
	}
	
	public double getWeightedBalance(){
		double bf = getBalanceFactor();
		return (bf == 0) ? 0 : -mirror.getCurrentBalance(this) * getRate() / bf;
	}
	
}