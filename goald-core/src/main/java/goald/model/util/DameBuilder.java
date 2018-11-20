package goald.model.util;

import goald.model.VE;

public class DameBuilder {
	
	protected VE dame;
	
	protected DameBuilder(){
		this.dame = new VE();
	}
	
	public static DameBuilder create(){
		return new DameBuilder();
	}
	
	public VE build(){
		VE built = this.dame;
		this.dame = null;
		return built;
	}
}
