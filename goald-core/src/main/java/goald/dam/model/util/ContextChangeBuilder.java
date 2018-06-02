package goald.dam.model.util;

import goald.dam.model.ContextChange;
import goald.dam.model.ContextChange.OP;

public class ContextChangeBuilder {
	
	protected ContextChange change;
	
	protected ContextChangeBuilder(){
		this.change = new ContextChange();
	}
	
	public static ContextChangeBuilder create(){
		return new ContextChangeBuilder();
	}
	
	public ContextChange build(){
		ContextChange built = this.change;
		this.change = null;
		return built;
	}

	public ContextChangeBuilder add(String label){
		this.change.setLabel(label);;
		this.change.setOp(OP.ADDED);
		return this;
	}

	public ContextChangeBuilder remove(String label) {
		this.change.setLabel(label);
		this.change.setOp(OP.REMOVED);
		return this;
	}
}
