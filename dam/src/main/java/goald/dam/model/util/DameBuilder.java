package goald.dam.model.util;

import goald.dam.model.Dame;

public class DameBuilder {
	
	protected Dame dame;
	
	protected DameBuilder(){
		this.dame = new Dame();
	}
	
	public static DameBuilder create(){
		return new DameBuilder();
	}
	
	public Dame build(){
		Dame built = this.dame;
		this.dame = null;
		return built;
	}
}
