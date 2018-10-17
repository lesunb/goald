package goald.design.model;

public class Node {

	protected String identification;
	protected String contextCondition;
	
	private Decomposition decomposition;
	
	public Node(String identification){
		this.identification = identification;
	}
	
	public Node(String identification, String contextCondition){
		this.identification = identification;
		this.contextCondition = contextCondition;
	}
	
	public String getIdentication(){
		return identification;
	}
	
	public Decomposition getDecomposition() {
		return decomposition;
	}

	public void setDecomposition(Decomposition decomposition) {
		this.decomposition = decomposition;
	}
	
	@Override
	public boolean equals(Object goal){
		return (goal instanceof Goal
				&& ((Goal) goal).identification.equals(identification)); 
	}



}
