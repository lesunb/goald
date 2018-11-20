package goald.analysis;

import goald.model.Agent;
import goald.model.Change.Effect;
import goald.model.ContextChange;
import goald.model.CtxEvaluator;
import goald.model.Deployment;
import goald.model.Deployment.BundleStatus;
import goald.model.Deployment.Status;

public class DeploymentChecker {
	
	private Agent agent;
	
	public DeploymentChecker(Agent agent) {
		this.agent = agent;
	}

	public boolean check(ContextChange change) {
		Deployment depl = agent.getDeployment();
		CtxEvaluator ctx = agent.getActualCtx();
		
		if(!agent.getRootDame().getIsAchievable()) {
			change.setEffect(Effect.NEUTRAL);
			return true;
		}
		
		boolean result = true;
		for(BundleStatus bundleStatus: depl.getBundles()) {
			//check for failure in active bundles
			boolean failure = bundleStatus.getStatus().equals(Status.ACTIVE)
					&& !ctx.check(bundleStatus.getBundle().getConditions());
			
			if(failure) {
				result = false;
				change.setEffect(Effect.FAILURE);
			}
		}
		return result;
	}
	
}
