package goald.design.model;

public class Interf extends ArchitectureUnit {
	
	public Interf(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "interface "+ name + " { }";
	}
}
