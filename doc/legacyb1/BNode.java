package legacyb1;

public class BNode{
	private Object value;
	private BNode left;
	private BNode right;
	
	public BNode(Object value){
		this.value = value;
		this.left = null;
		this.right = null;
	}
	
	public Object getValue(){
		return this.value;
	}
	
	public void setLeft(BNode bn){
		this.left = bn;
	}
	
	public BNode getLeft(){
		return this.left;
	}
	
	public void setRight(BNode bn){
		this.right = bn;
	}
	
	public BNode getRight(){
		return this.right;
	}
	
	public String toString(){
		return value.toString();
	}
	
}
	