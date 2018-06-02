package goald.events.model;

public class Change {


	public enum Operation {
		ADD,
		REM
	};
	
	public enum TargetType {
		GOAL,
		CONTEXT
	};
	
	public Operation op;
	
	public TargetType type;
	
	public String identification;
	
	public Change(Operation op, TargetType type, String identification) {
		super();
		this.op = op;
		this.type = type;
		this.identification = identification;
	}

	public Operation getOp() {
		return op;
	}

	public TargetType getType() {
		return type;
	}

	public String getIdentification() {
		return identification;
	}
	
}
