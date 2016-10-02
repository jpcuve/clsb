package legacyb1;

public class AccountAvailablePayOutComparator implements Comparator{
	
	public int compare(Object o1, Object o2){
		Account a1 = (Account)o1;
		Account a2 = (Account)o2;
		return (int)(a1.getAvailablePayOut() - a2.getAvailablePayOut());
	}
}