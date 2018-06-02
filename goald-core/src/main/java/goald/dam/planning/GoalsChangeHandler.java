package goald.dam.planning;

import java.util.List;
import java.util.stream.Collectors;

import goald.dam.model.Agent;
import goald.dam.model.Alternative;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.GoalsChangeRequest;
import goald.dam.model.GoalsChangeRequest.GoalChange;
import goald.dam.model.GoalsChangeRequest.GoalChangeOp;
import goald.dam.model.util.AlternativeBuilder;
import goald.dam.model.util.DameBuilder;
import goald.dam.model.util.RepoQueryBuilder;

public class GoalsChangeHandler {

	private DamUpdater updater;
	private Agent agent;
	
	public GoalsChangeHandler(DameRespository repo, Agent agent) {
		this.agent = agent;
		updater = new DamUpdater(repo, agent);
	}

	public void handle(GoalsChangeRequest goalsChangeRequest) {

		// TODO handle remove goal
		// TODO handle adding to existing goals
		if(goalsChangeRequest.getGoalChange().size() > 1) {
			// handle multiple root goals
			List<Goal> goals =  goalsChangeRequest.getGoalChange().stream()
					.filter((change) -> change.getOp() == GoalChangeOp.ADD)
					.map((change) -> change.getGoal()).collect(Collectors.toList());
			
			
			Dame rootDame = DameBuilder.create()
					.build();
			
			Alternative virtualAlt = AlternativeBuilder.create()
					.forDame(rootDame)
					.withDependencies(goals)
					.build();
			
			rootDame.getAlts().add(virtualAlt);
			updater.resolveDame(rootDame);
			agent.setRootDame(rootDame);
			// rootDame.setChosenAlt(virualAlt);
		} else {
			GoalChange change = goalsChangeRequest.getGoalChange().get(0);
			
			List<Goal> query = RepoQueryBuilder.create()
					.queryFor(change.getGoal())
					.build();
				
			Dame rootDame = updater.resolveGoals(query).get(0);
			agent.setRootDame(rootDame);
		}
	}

}
