package goalp.systems;

import java.util.HashSet;
import java.util.Set;

import goalp.model.Artifact;
import goalp.model.Goal;

public class DeploymentPlan {

	protected Set<Artifact> selectedArtifacts;


	public Set<Artifact> getSelectedArtifacts() {
		if(selectedArtifacts == null){
			selectedArtifacts = new HashSet<>();
		}
		return selectedArtifacts;
	}

	public void setSelectedArtifacts(Set<Artifact> selectedArtifacts) {
		this.selectedArtifacts = selectedArtifacts;
	}

	public boolean provides(Goal goal) {
		for(Artifact artifact: getSelectedArtifacts()){
			if(artifact.isProvider(goal)){
				return true;
			}
		}
		return false;
	}

}
