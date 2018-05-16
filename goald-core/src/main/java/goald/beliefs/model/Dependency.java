package goald.beliefs.model;

import java.util.List;

public class Dependency {

	public String identifier;
		
	public List<Bundle> alternatives;
	
	public Bundle definition;
	
	public DeploymentUnit selected;
	
	public enum DependencyStatus {
		UNDEFINED(false, null, false), // do not satisfied and unkwon if it can be satisfied
		INVALIDATED(false, null, false), // dependency satisfaction is not known any more, re-analyzing pending
		NOT_SATISFIEABLE(true, false, false), // dependency is beleive to not be satisfied in the actual context
		SATISFIEABLE(true, true, false), // dependency is beleive to not be satisfied in the actual context
		RESOLVED(true, true, false), // do not satisfied but it is expected to be satisfied after depl execution
		DEPLOYED(true, true, true); // dependency satisfied

		DependencyStatus(Boolean satisfieableStatusKnown, Boolean satisfieable, Boolean satisfing) {
		}
	}
	
	public DependencyStatus status = DependencyStatus.UNDEFINED;
	
}
