package goald.dam.planning;

import java.util.List;

import goald.dam.model.Agent;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.GoalsChangeRequest;
import goald.dam.model.GoalsChangeRequest.GoalChange;
import goald.dam.model.util.RepoQueryBuilder;

public class GoalsChangeHandler {

	private DamUpdater updater;
	private Agent agent;
	
	public GoalsChangeHandler(DameRespository repo, Agent agent) {
		this.agent = agent;
		updater = new DamUpdater(repo, agent);
	}

	public void handle(GoalsChangeRequest goalsChangeRequest) {
		// TODO handle multiple root goals
		// TODO handle remove goal
		if(goalsChangeRequest.getGoalChange().size() != 1) {
			throw new RuntimeException("plan for more then a root goal currently not supported");
		}
		
		GoalChange change = goalsChangeRequest.getGoalChange().get(0);
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor(change.getGoal())
				.build();
			
		Dame rootDame = updater.resolveGoals(query).get(0);		
		agent.setRootDame(rootDame);		
	}

}
