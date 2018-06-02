package goald.model;

import java.util.ArrayList;
import java.util.List;

import goald.beliefs.model.Bundle;
import goald.desires.model.Goal;

public class Component{

	protected String uuid;
	
	protected Bundle bundle;
	
	protected List<Goal> defines;
	
	protected List<Goal> provides;
	
	protected List<ComponentDependency> dependencies;
	
	protected List<Contribution> contributions;
	
	// protected List<Goal> implements;
	
	protected List<Goal> depends;
	
	protected List<ContextCondition> requires;


	public String getIdentification() {
		return this.uuid;
	}
	
	public List<Goal> getDefines() {
		if(this.defines == null){
			this.defines = new ArrayList<>();
		}
		return defines;
	}

	public void setDefines(List<Goal> defines) {
		this.defines = defines;
	}

	public List<Goal> getDependencies() {
		if(this.depends == null){
			this.depends = new ArrayList<>();
		}
		return depends;
	}

	public void setDependencies(List<Goal> dependencies) {
		this.depends = dependencies;
	}

	public List<String> getContextConditions() {
		if(contextRequirement == null){
			contextRequirement = new ArrayList<>();
		}
		return contextRequirement;
	}

	public void setContextRequirement(List<String> contextRequirement) {
		this.contextRequirement = contextRequirement;
	}
	
	public boolean isProvider(Goal goal) {
		for(Goal providedGoal: this.getProvide()){
			if(goal.equals(providedGoal)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Artifact [identification=" +uuid+ ", \n provide=" +  provide + ",\n dependencies=" + depends  
				+ ", \n contextRequirement=" + contextRequirement + "]";
	}

}
