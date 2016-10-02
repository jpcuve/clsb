package legacyb1;

public class CurrencyGroupPriorityComparator implements Comparator{
	public int compare(Object o1, Object o2){
		CurrencyGroup cg1 = (CurrencyGroup)o1;
		CurrencyGroup cg2 = (CurrencyGroup)o2;
		return (int)(cg1.getPriority() - cg2.getPriority());
	}
}