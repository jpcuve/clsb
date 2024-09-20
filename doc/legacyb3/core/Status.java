package legacyb3.core;

public class Status{
	public final static String MATCHED = "MA";
	public final static String UNMATCHED = "UN";
	public final static String BOOKED = "BK"; // for transactions, pay-ins and pay-outs
	public final static String READY = "RD"; // for pay-ins
	public final static String EXPIRED = "EX"; // for inputs, transactions
	public final static String AUTHORIZED = "AU"; // for inputs
	public final static String SETTLEMENTMATURE = "SM";
	public final static String SETTLED = "ST";
	public final static String SPLIT = "SP";
}
