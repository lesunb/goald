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

	public String getIdentication() {
		return this.identification;
	}

}
