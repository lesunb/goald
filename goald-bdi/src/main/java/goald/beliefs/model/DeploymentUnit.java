package goald.beliefs.model;

import java.util.ArrayList;
import java.util.List;

import goald.beliefs.model.Dependency.DependencyStatus;
import goald.model.Contribution;

public class DeploymentUnit {
	
	public enum DeploymentUnitType {
		ABSTRACT, // has not associated bundle, used as root
		CONCRETE; // has associated bundle
	}
	
	public DeploymentUnitType type;
	
	protected DependencyStatus status;
	
	protected Bundle provider;

	protected List<Dependency> dependencies;
	
	protected List<Contribution> contributions;
	
	public DependencyStatus getStatus() {
		return status;
	}

	public void setStatus(DependencyStatus status) {
		this.status = status;
	}

	public Bundle getProvider() {
		return provider;
	}

	public void setProvider(Bundle provider) {
		this.provider = provider;
	}
	
	public List<Dependency> getDependencies() {
		if(this.dependencies == null ) {
			this.dependencies = new ArrayList<>();
		}
		return dependencies;
	}

	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	public List<Contribution> getContributions() {
		return contributions;
	}

	public void setContributions(List<Contribution> contributions) {
		this.contributions = contributions;
	}


	
}
