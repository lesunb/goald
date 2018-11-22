package goald.model;

public class Dependency {

	public enum Modifier {
		ONE,
		ANY
	};
	
	private String identification;
	
	private Modifier modifier;
	
	public Dependency(String identification) {
		this.identification = identification;
		this.modifier = Modifier.ONE;
	}

	public Dependency(String identification, Modifier modifier) {
		this.identification = identification;
		this.modifier = modifier;
	}

	public String getIdentication() {
		return this.identification;
	}
	
	public Modifier getModifier() {
		return this.modifier;
	}

	@Override
	public String toString() {
		return "[" + modifier +":"+identification + "]";
	}

}
