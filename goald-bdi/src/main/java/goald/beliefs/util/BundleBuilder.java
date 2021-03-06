package goald.beliefs.util;

import goald.beliefs.model.Bundle;
import goald.desires.model.Goal;
import goald.model.ContextCondition;

public class BundleBuilder {
	
	protected Bundle bundle;
	
	protected BundleBuilder(){
		this.bundle = new Bundle();
	}
	
	public static BundleBuilder create(){
		return new BundleBuilder();
	}
	
	public Bundle build(){
		Bundle built = this.bundle;
		this.bundle = null;
		return built;
	}

	public BundleBuilder identification(String identification) {
		this.bundle.identification = identification;
		return this;
	}
	
	public BundleBuilder defines(String identification){
		this.bundle.getDefines().add((new Goal(identification)));
		return this;
	}

	public BundleBuilder provides(String identification){
		this.bundle.getProvides().add((new Goal(identification)));
		return this;
	}
	
	public BundleBuilder dependsOn(String identification){
		this.bundle.getDepends().add(new Goal(identification));
		return this;
	}
	
	public BundleBuilder dependsOn(String[] identifications){
		for(String identification:identifications){
			dependsOn(identification);
		}
		return this;
	}
	
	public BundleBuilder requires(String identification){
		this.bundle.getConditions().add((new ContextCondition(identification)));
		return this;
	}
	
	public BundleBuilder requires(String[] conditions){
		for(String condition:conditions){
			requires(condition);
		}
		return this;
	}

}
