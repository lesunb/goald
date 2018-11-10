package goald.design.model;

import java.util.ArrayList;
import java.util.List;

public class Component extends ArchitectureUnit {
	
	private String implementz;
	
	private List<String> requires;
	
	private ContextCondition contextCondition;
	
	public Component(String name) {
		super(name);
	}
	
	public Component(String name, String implementz, List<String> requires, ContextCondition contextCondition) {
		super(name);
		this.implementz = implementz;
		this.setRequires(requires);
		this.setContextCondition(contextCondition);
	}

	public String getImplementz() {
		return implementz;
	}

	public void setImplementz(String implementz) {
		this.implementz = implementz;
	}

	public List<String> getRequires() {
		if(this.requires == null) {
			this.requires = new ArrayList<>();
		}
		return this.requires;
	}

	public void setRequires(List<String> requires) {
		this.requires = requires;
	}
	
	public ContextCondition getContextCondition() {
		return contextCondition;
	}

	public void setContextCondition(ContextCondition contextCondition) {
		this.contextCondition = contextCondition;
	}
	
	@Override
	public String toString() {
		String conditionzStr = "";
		if(requires != null) {
			conditionzStr = 	"	requires " + requires + ";";
		}
		
		if(contextCondition != null) {
			conditionzStr = 	"	conditions " + contextCondition + ";";
		}
		
		return "component "+ name + " {"
			+	"	provides " + implementz + ";"
			+ 	conditionzStr
			+ "}";
	}
	
}
