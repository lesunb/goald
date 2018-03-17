package goald.beliefs.util;

import java.util.List;

import goald.beliefs.model.Bundle;
import goald.desires.model.Goal;
import goald.model.Contribution;
import goald.model.Dependency;

public class DeploymentUnit {

	String uuid;
	
	protected Bundle bundle;

	protected List<Goal> defines;
	
	protected List<Goal> provides;
	
	protected List<Dependency> dependencies;
	
	protected List<Contribution> contributions;
	
}
