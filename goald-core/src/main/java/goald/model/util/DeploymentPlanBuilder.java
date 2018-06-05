package goald.model.util;

import java.util.Set;

import goald.model.Bundle;
import goald.model.DeploymentPlan;
import goald.model.DeploymentPlan.Command;
import goald.model.DeploymentPlan.DeployOp;

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
		add(DeployOp.INSTALL, bundle);
		return this;
	}
	
	public DeploymentPlanBuilder add(DeployOp op, Bundle bundle) {
		Command command = this.plan.new Command(op, bundle);
		this.plan.getCommands().add(command);
		return this;
	}
	
	public DeploymentPlanBuilder install(Set<Bundle> bundles) {
		bundles.forEach( bundle -> install(bundle));
		return this;
	}

	public DeploymentPlanBuilder uninstall(Set<Bundle> bandles) {
		bandles.forEach( bundle -> add(DeployOp.UNINSTALL, bundle));
		return this;
	}
}
