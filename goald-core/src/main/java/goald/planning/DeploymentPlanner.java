package goald.planning;

import java.util.HashSet;
import java.util.Set;

import goald.model.GoalDManager;
import goald.model.Bundle;
import goald.model.VE;
import goald.model.DeploymentPlan;
import goald.model.Deployment.Status;
import goald.model.util.DeploymentPlanBuilder;
import goald.model.util.SetUtils;

public class DeploymentPlanner {
	
	DVMUpdater updater;
	
	GoalDManager agent;
	
	public DeploymentPlanner(VERespository repo, GoalDManager agent) {
		this.agent = agent;
		this.updater = new DVMUpdater(repo, agent);
	}
	
	
	public DeploymentPlan createPlan() {
		VE rootDame = this.agent.getRootDame();
		if(rootDame.getChosenAlt() == null) {		
			// deployment not possible
			// TODO how to procced?
			return DeploymentPlanBuilder.create()
					.uninstall(this.agent.getDeployment().getAll(Status.ACTIVE))
					.build();
		}
		
		Set<Bundle> sellected = addToSellected(rootDame, new HashSet<>());
		
		Set<Bundle> active = this.agent.getDeployment().getAll(Status.ACTIVE);
		
		SetUtils<Bundle> utils = new SetUtils<Bundle>();
		Set<Bundle> toInstall = utils.diffSet(sellected, active);
		Set<Bundle> toRemove = utils.diffSet(active, sellected);
		
		return DeploymentPlanBuilder.create()
				.install(toInstall)
				.uninstall(toRemove)
				.build();
	}
	
	public Set<Bundle> addToSellected(VE dame, Set<Bundle> sellected) {
		if(dame.getChosenAlt() == null) {
			// incomplete deployment plan
			return sellected;
		}
		
		if(dame.getParentAlt() == null && dame.getDefinition() == null) {
			// NOP ignoring def for root dame 
		}else {
			sellected.add(dame.getDefinition());
			sellected.add(dame.getChosenAlt().getImpl());
		}
		
		for(VE child:dame.getChosenAlt().getListDepVE()) {
			addToSellected(child, sellected);
		}
		return sellected;
	}
	
	public boolean isAreadyDeployed(Bundle bundle) {
		return this.agent.getDeployment().getStatus(bundle) == Status.ACTIVE;
	}
}
