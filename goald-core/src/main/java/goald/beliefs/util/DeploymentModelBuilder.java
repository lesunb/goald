package goald.beliefs.util;

import goald.beliefs.DeploymentModel;

public class DeploymentModelBuilder {

	private DeploymentModel model;

	private DeploymentModelBuilder() {
		this.model = new DeploymentModel();
	}

	public static DeploymentModelBuilder create() {
		return new DeploymentModelBuilder();
	}

	public DeploymentModel build() {
		DeploymentModel built = this.model;
		this.model = null;
		return built;
	}

	public DeploymentModelBuilder addFistLevelGoal(String identifier) {
		//this.model.add(bundle);
		return this;
	}
}
