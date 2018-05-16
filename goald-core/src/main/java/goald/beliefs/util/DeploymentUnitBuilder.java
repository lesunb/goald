package goald.beliefs.util;

import goald.beliefs.model.Bundle;
import goald.beliefs.model.Dependency;
import goald.beliefs.model.Dependency.DependencyStatus;
import goald.beliefs.model.DeploymentUnit;
import goald.beliefs.model.DeploymentUnit.DeploymentUnitType;

public class DeploymentUnitBuilder {

	private DeploymentUnit deploymentUnit;

	private DeploymentUnitBuilder() {
		this.deploymentUnit = new DeploymentUnit();
	}

	public static DeploymentUnitBuilder create() {
		return new DeploymentUnitBuilder();
	}

	public DeploymentUnit build() {
		DeploymentUnit built = this.deploymentUnit;
		this.deploymentUnit = null;
		return built;
	}

	public DeploymentUnitBuilder identifier(String identifier) {
		//this.model.add(bundle);
		return this;
	}
	
	public DeploymentUnitBuilder status(DependencyStatus status) {
		this.deploymentUnit.setStatus(status);
		return this;
	}
	
	public DeploymentUnitBuilder withDependency(Dependency dependency) {
		this.deploymentUnit.getDependencies().add(dependency);
		return this;
	}

	public DeploymentUnitBuilder bundle(Bundle alternative) {
		this.deploymentUnit.setProvider(alternative);
		return this;
	}

	public DeploymentUnitBuilder type(DeploymentUnitType type) {
		this.deploymentUnit.type = type;
		return this;
	}

}
