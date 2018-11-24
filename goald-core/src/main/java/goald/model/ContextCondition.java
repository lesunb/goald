package goald.model;

import java.util.List;

public class ContextCondition {
	
	private String label;

	public ContextCondition() {}
	
	public ContextCondition(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
