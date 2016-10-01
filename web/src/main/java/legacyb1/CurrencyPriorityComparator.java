package legacyb1;

public class CurrencyPriorityComparator implements Comparator{
	public int compare(Object o1, Object o2){
		Currency c1 = (Currency)o1;
		Currency c2 = (Currency)o2;
		double diff = c2.getWeightedBalance() - c1.getWeightedBalance();
		return (diff == 0) ? 0 : ((diff < 0) ? -1 : 1);
	}
}