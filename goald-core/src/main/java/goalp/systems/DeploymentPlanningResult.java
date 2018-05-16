package goalp.systems;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.List;

import goalp.model.Artifact;

public class DeploymentPlanningResult {

	public DeploymentPlan plan;
	
	public Deque<String> failures;
	
	/**
	 * Time of clock took in planning in milisecons.
	 */
	protected Long planDuration;

	protected Date startTime;
	
	public DeploymentPlanningResult(){
		startTime = new Date();
	}
	
	public void stop(){
		planDuration = startTime.getTime() - (new Date()).getTime();
	}
	
	public boolean isSuccessfull(){
		if(getFailures().isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public Deque<String> getFailures(){
		if(failures == null){
			failures = new ArrayDeque<>();;
		}
		return failures;
	}
	
	public void putFailure(String failure){
		 getFailures().push(failure);
	}

	public void putFailures(List<String> failures){
		 getFailures().addAll(failures);
	}
	
	public void incorporate(DeploymentPlan subPlan) {
		getPlan().getSelectedArtifacts().addAll(subPlan.getSelectedArtifacts());
	}
	
	public void incorporate(Artifact artifact) {
		getPlan().getSelectedArtifacts().add(artifact);
	}
	
	public DeploymentPlan getPlan() {
		if(plan == null){
			plan = new DeploymentPlan();
		}
		return plan;
	}
	
	public int getPlanSize(){
		return getPlan().getSelectedArtifacts().size();
	}
	
	public boolean isPresentInThePlan(String identification){
		for(Artifact artifact:getPlan().getSelectedArtifacts()){
			if(artifact.getIdentification().equals(identification)){
				return true;
			}
		}
	
		return false;
	}


	
}
