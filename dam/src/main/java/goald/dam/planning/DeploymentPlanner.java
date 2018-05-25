package goald.dam.planning;

import goald.dam.model.Agent;
import goald.dam.model.Dame;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.util.DeploymentPlanBuilder;

public class DeploymentPlanner {
	
	DamUpdater updater;
	
	Agent agent;
	
	public DeploymentPlanner(DameRespository repo, Agent agent) {
		this.agent = agent;
		this.updater = new DamUpdater(repo, agent);
	}
	
	
	public DeploymentPlan createPlan(Dame rootDame) {
		DeploymentPlanBuilder builder = DeploymentPlanBuilder.create();
		addToPlan(rootDame, builder);		
		return builder.build();
	}
	
	public void addToPlan(Dame dame, DeploymentPlanBuilder builder) {
		builder.install(dame.getDefinition());
		builder.install(dame.getChosenAlt().getImpl());
		
		for(Dame child:dame.getChosenAlt().getListDepDame()) {
			addToPlan(child, builder);
		}
	}
	
}
