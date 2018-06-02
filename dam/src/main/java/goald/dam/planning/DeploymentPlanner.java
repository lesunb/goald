package goald.dam.planning;

import java.util.HashSet;
import java.util.Set;

import goald.dam.model.Agent;
import goald.dam.model.Bundle;
import goald.dam.model.Dame;
import goald.dam.model.Deployment.Status;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.util.DeploymentPlanBuilder;
import goald.dam.model.util.SetUtils;

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
		sellected.add(dame.getDefinition());
		if(dame.getChosenAlt() == null) {
			System.out.println("incomplete deployment plan");
			return sellected;
		}
		sellected.add(dame.getChosenAlt().getImpl());
		
		for(Dame child:dame.getChosenAlt().getListDepDame()) {
			addToSellected(child, sellected);
		}
		return sellected;
	}
	
	public boolean isAreadyDeployed(Bundle bundle) {
		return this.agent.getDeployment().getStatus(bundle) == Status.ACTIVE;
	}
}
