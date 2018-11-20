package goald.planning;

import java.util.List;
import java.util.stream.Collectors;

import goald.model.GoalDManager;
import goald.model.Alternative;
import goald.model.VE;
import goald.model.Goal;
import goald.model.GoalsChangeRequest;
import goald.model.GoalsChangeRequest.GoalChange;
import goald.model.GoalsChangeRequest.GoalChangeOp;
import goald.model.util.AlternativeBuilder;
import goald.model.util.DameBuilder;
import goald.model.util.RepoQueryBuilder;

public class GoalsChangeHandler {

	private DamUpdater updater;
	private GoalDManager agent;
	
	public GoalsChangeHandler(DameRespository repo, GoalDManager agent) {
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
			
			
			VE rootDame = DameBuilder.create()
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
				
			VE rootDame = updater.resolveGoals(query).get(0);
			agent.setRootDame(rootDame);
		}
	}

}
