package legacyb1;

import java.util.*;

public class QuickSorter extends Sorter{
	
	public void sort(Comparator c, Vector v){
		quickSort(c, v, 0, v.size() - 1);
	}
	
	private void quickSort(Comparator c, Vector v, int lo0, int hi0){
		int lo = lo0;
		int hi = hi0;
		if (lo >= hi) return;
		Object mid = v.elementAt((lo + hi) / 2);
		while(lo < hi){
			while(lo < hi && c.compare(v.elementAt(lo), mid) < 0) lo++;
			while(lo < hi && c.compare(v.elementAt(hi), mid) > 0) hi--;
			if(lo < hi){
				Object T = v.elementAt(hi);
				v.setElementAt(v.elementAt(lo), hi);
				v.setElementAt(T, lo);
			}
		}
		if(hi < lo){
			int T = hi;
			hi = lo;
			lo = T;
		}
		quickSort(c, v, lo0, lo);
		quickSort(c, v, lo == lo0 ? lo + 1 : lo, hi0);
	}
	
}