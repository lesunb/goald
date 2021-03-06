package goald.desires.model;

import goald.IDependency;

public class Goal implements IDependency {

	protected String identification;
	
	public Goal(String identification){
		this.identification = identification;
	}
	
	public String getIdentication(){
		return identification;
	}
	
	@Override
	public boolean equals(Object goal){
		return (goal instanceof Goal
				&& ((Goal) goal).identification.equals(identification)); 
	}
	
	@Override
	public String toString() {
		return "Goal [identification=" + identification + "]";
	}
	
	
}
