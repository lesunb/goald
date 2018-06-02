package goalp.model;

import java.util.ArrayList;
import java.util.List;

import goalp.model.Artifact;
import goalp.model.Goal;

public class Agent {

	protected List<String> context;

	protected List<Resource> resources;

	protected List<Artifact> localRepository;

	protected List<Goal> achievableGoals;


	public List<String> getContext() {
		if(context == null){
			context = new ArrayList<>();
		}
		return context;
	}

	public void setContext(List<String> context) {
		this.context = context;
	}
	
	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public List<Artifact> getLocalRepository() {
		return localRepository;
	}

	public void setLocalRepository(List<Artifact> localRepository) {
		this.localRepository = localRepository;
	}

	public List<Goal> getAchievableGoals() {
		return achievableGoals;
	}

	public void setAchievableGoals(List<Goal> achievableGoals) {
		this.achievableGoals = achievableGoals;
	}

}
