package legacyb1;

public class CurrencyFCTTComparator implements Comparator{
	public int compare(Object o1, Object o2){
		Currency c1 = (Currency)o1;
		Currency c2 = (Currency)o2;
		TimeOfDay fctt1 = c1.getFCTT();
		TimeOfDay fctt2 = c2.getFCTT();
		return (fctt1.isAfter(fctt2)) ? 1 : ((fctt1.isBefore(fctt2)) ? -1 : 0);
	}
}