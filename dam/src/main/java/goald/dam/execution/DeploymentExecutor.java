package goald.dam.execution;

import goald.dam.model.Agent;
import goald.dam.model.Deployment;
import goald.dam.model.Deployment.Status;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.DeploymentPlan.Command;
import goald.dam.model.DeploymentPlan.DeployOp;

public class DeploymentExecutor {
	
	private Agent agent;
	
	public DeploymentExecutor(Agent agent) {
		this.agent = agent;
	}

	public void execute(DeploymentPlan plan) {
		Deployment depl = agent.getDeployment();
		
		for(Command command:plan.getCommands()) {
			if(command.getOp() == DeployOp.INSTALL ) {
				depl.add(Status.ACTIVE, command.getBundle());
			}
			if(command.getOp() == DeployOp.UNINSTALL ) {
				depl.add(Status.UNINSTALLED, command.getBundle());
			}
		}
	}
}
