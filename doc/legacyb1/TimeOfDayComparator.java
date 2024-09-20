package legacyb1;

public class TimeOfDayComparator implements Comparator{
	public int compare(Object o1, Object o2){
		TimeOfDay tod1 = (TimeOfDay)o1;
		TimeOfDay tod2 = (TimeOfDay)o2;
		return tod1.getMinutes() - tod2.getMinutes();
	}
}
