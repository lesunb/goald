package goald.execution;

import goald.model.Agent;
import goald.model.Deployment;
import goald.model.DeploymentPlan;
import goald.model.Deployment.Status;
import goald.model.DeploymentPlan.Command;
import goald.model.DeploymentPlan.DeployOp;

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
				depl.remove(command.getBundle());
			}
		}
	}
}
