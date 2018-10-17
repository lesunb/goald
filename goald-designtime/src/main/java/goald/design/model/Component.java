package goald.design.model;

import java.util.List;

public class Component extends ArchitectureUnit {
	
	private String implementz;
	
	private List<String> requires;
	
	public Component(String name) {
		super(name);
	}
	
	public Component(String name, String implementz, List<String> requires) {
		super(name);
		this.implementz = implementz;
		this.setRequires(requires);
	}

	public String getImplementz() {
		return implementz;
	}

	public void setImplementz(String implementz) {
		this.implementz = implementz;
	}

	public List<String> getRequires() {
		return requires;
	}

	public void setRequires(List<String> requires) {
		this.requires = requires;
	}
	
	@Override
	public String toString() {
		String conditionzStr = "";
		if(requires != null) {
			conditionzStr = 	"	conditions " + requires + ";";
		}
		
		return "component "+ name + " {"
			+	"	provides " + implementz + ";"
			+ 	conditionzStr
			+ "}";
	}
	
}
