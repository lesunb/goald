package goald.eval.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import goald.model.GoalDManager;
import goald.repository.IRepository;

public class ExperimentSetup {

	private IRepository repository;
	
	private Map<Integer, List<String>> rootGoals;
	
	// private IDeploymentPlanner planner;
	
	private GoalDManager agent;

	private List<String> contextSpace;

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

//	public IDeploymentPlanner getPlanner() {
//		return planner;
//	}
//
//	public void setPlanner(IDeploymentPlanner planner) {
//		this.planner = planner;
//	}

	public GoalDManager getAgent() {
		return agent;
	}

	public void setAgent(GoalDManager agent) {
		this.agent = agent;
	}
	
	public List<String> getCtxSpace() {
		return this.contextSpace;
	}

	public void setCtxSpace(List<String> contextSpace) {
		this.contextSpace = contextSpace;
	}

}
