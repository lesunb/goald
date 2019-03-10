package goald.model.util;

import java.util.ArrayList;
import java.util.List;

import goald.model.Bundle;
import goald.model.ContextCondition;
import goald.model.Dependency;
import goald.model.DependencyModifier;
import goald.model.DependencyModifier.Type;
import goald.model.Goal;
import goald.model.QualityParameter;

public class BundleBuilder {
	
	protected Bundle bundle;
	
	int anyGroupId = 0;
	
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
		this.bundle.getDepends().add(new Dependency(Type.ONE, identification));
		return this;
	}
	
	public BundleBuilder dependsOnAny(String ...identifications){
		for(String identification: identifications) {
			this.bundle.getDepends().add(new Dependency(Type.ANY, identification, anyGroupId++));	
		}		 
		return this;
	}
	
	public BundleBuilder dependsOn(DependencyModifier.Type modifier, String identification){
		this.bundle.getDepends().add(new Dependency(modifier, identification));
		return this;
	}
	
	public BundleBuilder dependsOnCond(String context, String identification) {
		List<ContextCondition> conditions = new ArrayList<>();
		conditions.add(new ContextCondition(context));
		
		this.bundle.getDepends().add(new Dependency( 
				new DependencyModifier(DependencyModifier.Type.COND, conditions), identification));
		return this;
	}
	
	public BundleBuilder dependsOn(String[] identifications){
		for(String identification:identifications){
			dependsOn(identification);
		}
		return this;
	}
	
//	public BundleBuilder dependsOnAny(String[] identifications){
//		for(String identification:identifications){
//			dependsOn(identification, DependencyModifier.Type.ANY);
//		}
//		return this;
//	}
//	
	public BundleBuilder condition(String identification){
		this.bundle.getConditions().add((new ContextCondition(identification)));
		return this;
	}
	
	public BundleBuilder condition(String[] conditions){
		for(String condition:conditions){
			condition(condition);
		}
		return this;
	}

	public BundleBuilder withQuality(String label, int value) {
		this.bundle.getQualityParams().add(new QualityParameter(label, value));
		return this;
	}
	
}
