package goald.dam.model.util;

import goald.dam.model.Bundle;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.DeploymentPlan.Command;
import goald.dam.model.DeploymentPlan.DeployOp;

public class DeploymentPlanBuilder {
	
	protected DeploymentPlan plan;
	
	protected DeploymentPlanBuilder(){
		this.plan = new DeploymentPlan();
	}
	
	public static DeploymentPlanBuilder create(){
		return new DeploymentPlanBuilder();
	}
	
	public DeploymentPlan build(){
		DeploymentPlan built = this.plan;
		this.plan = null;
		return built;
	}
	
	public DeploymentPlanBuilder install(Bundle bundle) {
		Command command = this.plan.new Command(DeployOp.INSTALL, bundle);
		this.plan.getCommands().add(command);
		return this;
	}

}
