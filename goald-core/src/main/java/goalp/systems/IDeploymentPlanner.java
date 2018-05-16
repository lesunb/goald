package goalp.systems;

import goalp.model.Agent;
import goalp.model.DeploymentRequest;

//Goal
public interface IDeploymentPlanner {

	DeploymentPlanningResult doPlan(DeploymentRequest request, Agent agent) throws PlanSelectionException;

}
