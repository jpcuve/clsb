package legacyb1;

import java.util.*;

public class BTree{
	private Comparator c;
	private BNode root;
	private int size;
	
	public BTree(Comparator c){
		this.c = c;
		this.root = null;
		this.size = 0;
	}
	
	private boolean addBNode(BNode cur, BNode bn){
		int compRes = c.compare(bn.getValue(), cur.getValue());
		if(compRes < 0 || (compRes == 0 && !cur.getValue().equals(bn.getValue()))){
			BNode left = cur.getLeft();
			if(left == null){
				cur.setLeft(bn);
				return true;
			}else{
				addBNode(left, bn);
			}
		}
		if(compRes > 0){
			BNode right = cur.getRight();
			if(right == null){
				cur.setRight(bn);
				return true;
			}else{
				addBNode(right, bn);
			}
		}
		return false;
	}
	
	public void addElement(Object o){
		BNode bn = new BNode(o);
		if (root == null){
			root = bn;
			size++;
		}else{
			if (addBNode(root, bn)) size++;
		}
	}
	
	private void walkNodeLeftToRight(BNode cur, Vector v){
		BNode bn;
		if ((bn = cur.getLeft()) != null) walkNodeLeftToRight(bn, v);
		v.addElement(cur);
		if ((bn = cur.getRight()) != null) walkNodeLeftToRight(bn, v);
	}
	
	private void walkNodeRightToLeft(BNode cur, Vector v){
		BNode bn;
		if ((bn = cur.getRight()) != null) walkNodeRightToLeft(bn, v);
		v.addElement(cur);
		if ((bn = cur.getLeft()) != null) walkNodeRightToLeft(bn, v);
	}
		
	public void copyInto(Vector v, boolean dir){
		if (root != null){
			if (dir) walkNodeLeftToRight(root, v); else walkNodeRightToLeft(root, v);
		}
	}
	
	private boolean _contains(BNode cur, Object o){
		int compRes = c.compare(o, cur.getValue());
		BNode bn;
		if(compRes < 0){
			if((bn = cur.getLeft()) != null){
				return _contains(bn, o);
			}else{
				return false;
			}
		}
		if(compRes > 0){
			if((bn = cur.getRight()) != null){
				return _contains(bn, o);
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean contains(Object o){
		boolean retVal = false;
		if (root != null){
			retVal = _contains(root, o);
		}
		return retVal;
	}
	
	public Object clone(){
		BTree bt = new BTree(c);
		for(Enumeration e1 = elements(); e1.hasMoreElements();) bt.addElement(e1.nextElement());
		return bt;
	}
	
	public Object firstElement(){
		return root;
	}
	
	public Enumeration elements(){
		return new BTreeIterator(this, true);
	}
	
	public Enumeration elements(boolean dir){
		return new BTreeIterator(this, dir);
	}
		
	
	public boolean isEmpty(){
		return (size == 0);
	}
	
	public int size(){
		return size;
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer(1024);
		for(Enumeration e1 = elements(); e1.hasMoreElements();){
			s.append(e1.nextElement());
			s.append(" ");
		}
		return s.toString();
	}
}