package goalp.evaluation.model;

import goalp.model.DeploymentRequest;
import goalp.systems.DeploymentPlanningResult;

public class ExecResult {
	DeploymentRequest request;

	DeploymentPlanningResult resultPlan;

	public ExecResult(DeploymentRequest request, DeploymentPlanningResult resultPlan) {
		this.request = request;
		this.resultPlan = resultPlan;
	}
	public ExecResult(){
		
	}
	
	public DeploymentRequest getRequest() {
		return request;
	}

	public void setRequest(DeploymentRequest request) {
		this.request = request;
	}

	public DeploymentPlanningResult getResultPlan() {
		return resultPlan;
	}

	public void setResultPlan(DeploymentPlanningResult resultPlan) {
		this.resultPlan = resultPlan;
	}

}
