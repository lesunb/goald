package goald.dam.model.util;

import goald.dam.model.Bundle;
import goald.dam.model.ContextCondition;
import goald.dam.model.Goal;
import goald.dam.model.QualityParameter;

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

	public BundleBuilder withQuality(String label, int value) {
		this.bundle.getQualityParams().add(new QualityParameter(label, value));
		return this;
	}

}
