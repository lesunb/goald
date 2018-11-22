package goald.model;

import java.util.List;

import goald.model.DependencyModifier.Type;

public class Dependency {

	private String identification;
	
	private DependencyModifier modifier;
	
	public Dependency(String identification) {
		this.identification = identification;
		this.modifier = new DependencyModifier(Type.ONE);
	}

	public Dependency(String identification, DependencyModifier.Type modifier) {
		this.identification = identification;
		this.modifier = new DependencyModifier(Type.ONE);
	}
	
	public Dependency(String identification, 
			DependencyModifier.Type modifier, 
			 List<ContextCondition> conditions) {
		this.identification = identification;
		this.modifier = new DependencyModifier(modifier, conditions);
	}

	public String getIdentication() {
		return this.identification;
	}
	
	public DependencyModifier getModifier() {
		return this.modifier;
	}

	@Override
	public String toString() {
		return "[" + modifier +":"+identification + "]";
	}

}
