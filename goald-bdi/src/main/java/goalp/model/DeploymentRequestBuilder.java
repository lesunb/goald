package goalp.model;

import java.util.List;

import goald.desires.model.Goal;
import goald.events.DeploymentRequest;

public class DeploymentRequestBuilder {

	private DeploymentRequest deploymentRequest;

	private DeploymentRequestBuilder() {
		this.deploymentRequest = new DeploymentRequest();
	}

	public static DeploymentRequestBuilder create() {
		return new DeploymentRequestBuilder();
	}

	public DeploymentRequest build() {
		DeploymentRequest built = this.deploymentRequest;
		this.deploymentRequest = null;
		return built;
	}

	public DeploymentRequestBuilder addGoal(String goalIdentification) {
		this.deploymentRequest.getGoals().add((new Goal(goalIdentification)));
		return this;
	}

	public DeploymentRequestBuilder addGoals(List<String> goalIdentifications) {
		goalIdentifications.forEach(goalIdentification ->{
			addGoal(goalIdentification);			
		});
		return this;
	}
}
