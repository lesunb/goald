package goald.design.model;

public class Goal extends Node{
	
	public Goal(String identification) {
		super(identification);
	}

	@Override
	public String toString() {
		return "Goal [identification=" + identification + "]";
	}	
}
