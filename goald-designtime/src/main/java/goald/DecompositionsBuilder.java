package goald;

import java.util.List;

import goald.design.model.Decomposition;
import goald.design.model.SubDecomposition;

public class DecompositionsBuilder {

	Decomposition entity;
	String fromIdentification;
	List<SubDecomposition> subDecomposition;
	
	private DecompositionsBuilder() {
		this.entity = new Decomposition();
	}
	
	public static DecompositionsBuilder create() {		
		return new DecompositionsBuilder();
	}

	public DecompositionsBuilder and() {
		this.entity.setType(Decomposition.Type.AND);
		return this;
	}
	
	public DecompositionsBuilder or() {
		this.entity.setType(Decomposition.Type.OR);
		return this;
	}

	public DecompositionsBuilder from(String fromIdentification) {
		this.fromIdentification = fromIdentification;
		return this;
	}
	
	public DecompositionsBuilder intoTask(String toIdentification) {
		return this.intoTask(toIdentification, null);
	}

	public DecompositionsBuilder intoTask(String toIdentification, String contextCondition) {
		this.addSubDecomposition(new SubDecomposition(toIdentification, contextCondition));
		return this;
	}

	public void addSubDecomposition(SubDecomposition subDecomposition) {
		this.entity.getSubDecompositions().add(subDecomposition);
	}

	public Decomposition build() {
		return entity;
	}
}
