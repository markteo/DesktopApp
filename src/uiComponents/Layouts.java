package uiComponents;


public enum Layouts{
	border("border"),box("box"),card("card"),flow("flow"),grid("grid");
	
	String layout;
	Layouts(String layoutType){
		this.layout=layoutType;
	}
	
	public String showName(){
		return this.layout;
	}
}