package goald.model.util;

import goald.model.Dependency;
import goald.model.VE;

public class VEBuilder {
	
	protected VE ve;
	
	protected VEBuilder(){
		this.ve = new VE();
	}
	
	public static VEBuilder create(){
		return new VEBuilder();
	}
	
	public VE build(){
		VE built = this.ve;
		this.ve = null;
		return built;
	}

	public VEBuilder virtualDependency() {
		this.ve.setSatisfy(new Dependency("virtual-root-goal"));
		return this;
	}
}
