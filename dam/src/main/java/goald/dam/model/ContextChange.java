package goald.dam.model;

public class ContextChange {
	public enum OP {
		ADDED,
		REMOVED
	};
	
	private OP op;
	
	private String label;

	public ContextChange(OP op, String label) {
		this.op = op;
		this.label = label;
	}

	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
