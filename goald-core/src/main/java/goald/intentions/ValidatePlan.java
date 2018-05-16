package goald.intentions;

import java.util.Set;

import goalp.model.Artifact;
import goalp.model.DeploymentRequest;
import goalp.model.Goal;
import goalp.systems.DeploymentPlanningResult;

public class ValidatePlan {

	public enum ValidationResult {
		OK,
		NOT_ALL_GOALS_ACHIEVABLE
	}

	
	public ValidationResult exec(DeploymentRequest request, DeploymentPlanningResult result){
		ValidationResult validation = ValidationResult.OK;
		for(Goal goal:request.getGoals()){
			if(!isAchievable(goal, result.getPlan().getSelectedArtifacts())){
				validation = ValidationResult.NOT_ALL_GOALS_ACHIEVABLE;
			}
		}
		//TODO validate context
		return validation;
	}
	
	private boolean isAchievable(Goal goal, final Set<Artifact> selectedArtifacts) {
		boolean isAchievable = false;
		for(Artifact artifact:selectedArtifacts){
			if(artifact.getProvide().contains(goal)){
				isAchievable = true;
				for(Goal goalDep:artifact.getDependencies()){
					isAchievable &= isAchievable(goalDep, selectedArtifacts) ;
				}
			}
		}
		return isAchievable;
	}
}
