package goald.design.model;

public class SubDecomposition {

	private Node into;
	
	public ContextCondition contextCondition;

	private String intoIdentification;

	public SubDecomposition(String into, String contextCondition) {
		this.setIntoIdentification(into);
		
		if(contextCondition != null) {
			this.contextCondition = new ContextCondition(contextCondition);	
		}
	}
	
	public SubDecomposition(Node into, ContextCondition contextCondition) {
		this.into = into;
		this.contextCondition = contextCondition;
	}
	
	public String getIntoIdentification() {
		return intoIdentification;
	}

	public void setIntoIdentification(String intoIdentification) {
		this.intoIdentification = intoIdentification;
	}
	
	public Node getInto() {
		return this.into;
	}
	
	public void setInto(Node into) {
		this.into = into;
	}
	
	public ContextCondition getContextCondition() {
		return contextCondition;
	}

	public void setContextCondition(ContextCondition contextCondition) {
		this.contextCondition = contextCondition;
	}

}
