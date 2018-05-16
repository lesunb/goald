package goalp.evaluation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import goald.beliefs.model.IRepository;
import goalp.model.Agent;
import goalp.systems.IDeploymentPlanner;

public class ExperimentSetup {

	private IRepository repository;
	
	private Map<Integer, List<String>> rootGoals;
	
	private IDeploymentPlanner planner;
	
	private Agent agent;

	public IRepository getRepository() {
		return repository;
	}

	public void setRepository(IRepository repository) {
		this.repository = repository;
	}

	public List<String> getRootGoals(Integer variability) {
		return rootGoals.get(variability);
	}
	
	public List<String> getRootGoals() {
		List<String> allRootGoals = new ArrayList<>();
		rootGoals.values().forEach(list ->{
			allRootGoals.addAll(list);
		});
		return allRootGoals;
	}

	public void setRootGoalsMap(Map<Integer, List<String>> rootGoals) {
		this.rootGoals = rootGoals;
	}

	public IDeploymentPlanner getPlanner() {
		return planner;
	}

	public void setPlanner(IDeploymentPlanner planner) {
		this.planner = planner;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

}
