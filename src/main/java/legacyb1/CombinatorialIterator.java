package legacyb1;

import java.util.Enumeration;

public class CombinatorialIterator implements Enumeration{
	private int bagsize;
	private int picksize;
	private int internalcounter;
	private int[] a;
	
	public CombinatorialIterator(int bs, int ps){
		bagsize = bs;
		picksize = Math.min(ps, bs);
		internalcounter = 0;
		a = new int[picksize];
		for(int j = 0; j < this.picksize; j++) a[j] = j;
		a[picksize - 1]--;
	}
	
	public boolean hasMoreElements(){
		for(internalcounter = 0; internalcounter < picksize; internalcounter++)
			if(a[picksize - 1 - internalcounter] != bagsize - internalcounter - 1) break;
		return (internalcounter < picksize);
	}
	
	public Object nextElement(){
		a[picksize - internalcounter - 1]++;
		for(int j = picksize - internalcounter; j < picksize; j++) a[j] = a[j - 1] + 1;
		return a;
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		s.append("[ ");
		for(int i = 0; i < picksize; i++){
			s.append(a[i]);
			s.append(" ");
		}
		s.append("]");
		return s.toString();
	}
	
	public static void main(String[] args){
		int i = 1;
		for(Enumeration e = new CombinatorialIterator(7, 3); e.hasMoreElements();){
			e.nextElement();
			System.out.println(" " + i++ + ": "+ e);
		}
	}

}
	
	