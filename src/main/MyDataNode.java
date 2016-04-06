package main;

import javax.swing.tree.DefaultMutableTreeNode;

public class MyDataNode extends DefaultMutableTreeNode{
	private String value;
	public MyDataNode(String value, String name){
		super(name);
		this.setValue(value);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
