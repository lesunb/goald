package goald.model;

import goald.model.DependencyModifier.Type;

public class Dependency {

	private String identification;
	
	private DependencyModifier modifier;
		
	public Dependency(String identification) {
		super();
		this.identification = identification;
		this.modifier = new DependencyModifier(Type.ONE);
	}
	
	public Dependency(DependencyModifier modifier, String identification) {
		super();
		this.identification = identification;
		this.modifier = modifier;
	}
	
	public Dependency(DependencyModifier.Type type, String identification) {
		super();
		this.identification = identification;
		this.modifier = new DependencyModifier(type);
	}
	
	public Dependency(DependencyModifier.Type type, String identification, int groupId) {
		super();
		this.identification = identification;
		this.modifier = new DependencyModifier(type, groupId);
	}

	public String getIdentification() {
		return identification;
	}


	public DependencyModifier getModifier() {
		return modifier;
	}


	@Override
	public String toString() {
		return "[" + modifier +":"+identification + "]";	
	}

}
