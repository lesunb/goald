package goald.events.utils;

import java.util.List;
import java.util.UUID;

import goald.events.DeploymentRequest;
import goald.events.model.Change;
import goald.events.model.Change.Operation;
import goald.events.model.Change.TargetType;

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
		this.deploymentRequest.getChanges().add(
				new Change(Operation.ADD, TargetType.GOAL, goalIdentification));
		return this;
	}

	public DeploymentRequestBuilder addGoals(List<String> goalIdentifications) {
		goalIdentifications.forEach(goalIdentification ->{
			addGoal(goalIdentification);			
		});
		return this;
	}

	public DeploymentRequestBuilder to(UUID uuid) {
		this.deploymentRequest.target = uuid;
		return this;
	}
}
