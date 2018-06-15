package goald.planning;

import java.util.HashSet;
import java.util.Set;

import goald.model.Agent;
import goald.model.Bundle;
import goald.model.Dame;
import goald.model.DeploymentPlan;
import goald.model.Deployment.Status;
import goald.model.util.DeploymentPlanBuilder;
import goald.model.util.SetUtils;

public class DeploymentPlanner {
	
	DamUpdater updater;
	
	Agent agent;
	
	public DeploymentPlanner(DameRespository repo, Agent agent) {
		this.agent = agent;
		this.updater = new DamUpdater(repo, agent);
	}
	
	
	public DeploymentPlan createPlan() {
		Dame rootDame = this.agent.getRootDame();
		if(rootDame.getChosenAlt() == null) {
			// deployment not possible
			// TODO how to procced?
			return DeploymentPlanBuilder.create()
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
	
	public Set<Bundle> addToSellected(Dame dame, Set<Bundle> sellected) {
		if(dame.getChosenAlt() == null) {
			System.out.println("incomplete deployment plan");
			return sellected;
		}
		
		if(dame.getParentAlt() == null && dame.getDefinition() == null) {
			System.out.println("ignoring def for root dame");
		}else {
			sellected.add(dame.getDefinition());
			sellected.add(dame.getChosenAlt().getImpl());
		}
		
		
		for(Dame child:dame.getChosenAlt().getListDepDame()) {
			addToSellected(child, sellected);
		}
		return sellected;
	}
	
	public boolean isAreadyDeployed(Bundle bundle) {
		return this.agent.getDeployment().getStatus(bundle) == Status.ACTIVE;
	}
}
