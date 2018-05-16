package goalp.systems;

import java.util.ArrayList;
import java.util.List;

import goald.beliefs.model.IRepository;
import goalp.model.Artifact;
import goalp.model.Goal;

public class SimpleListRepository implements IRepository {

	protected List<Artifact> knownArtifacts;
	
	int repoSize = 0;

	/* (non-Javadoc)
	 * @see goalp.systems.IRepository#getKnownArtifacts()
	 */
	protected List<Artifact> getKnownArtifacts() {
		if(knownArtifacts == null){
			knownArtifacts = new ArrayList<>();
		}
		return knownArtifacts;
	}

	@Override
	public void addArtifact(Artifact artifact) {
		repoSize++;
		getKnownArtifacts().add(artifact);
	}
	
	public int getSize(){
		return repoSize;
	}
	
	/* (non-Javadoc)
	 * @see goalp.systems.IRepository#getArtifactsThatProvideGoal(java.lang.String)
	 */
	@Override
	public List<Artifact>  getArtifactsThatProvidesGoal(Goal goal){ 
		List<Artifact> artifacts = new ArrayList<>();
		for(Artifact artifact:this.knownArtifacts){
			if(artifact.isProvider(goal)){
				artifacts.add(artifact);
			}
		}
		return artifacts;
	}

}