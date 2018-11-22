package goald.planning;

import java.util.List;
import java.util.stream.Collectors;

import goald.model.Alternative;
import goald.model.Dependency;
import goald.model.Goal;
import goald.model.GoalDManager;
import goald.model.GoalsChangeRequest;
import goald.model.GoalsChangeRequest.GoalChange;
import goald.model.GoalsChangeRequest.GoalChangeOp;
import goald.model.VE;
import goald.model.util.AlternativeBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.model.util.VEBuilder;

public class GoalsChangeHandler {

	private DVMUpdater updater;
	private GoalDManager goalDmanager;
	
	public GoalsChangeHandler(VERespository repo, GoalDManager agent) {
		this.goalDmanager = agent;
		updater = new DVMUpdater(repo, agent);
	}

	public void handle(GoalsChangeRequest goalsChangeRequest) {

		// TODO handle remove goal
		// TODO handle adding to existing goals
		if(goalsChangeRequest.getGoalChange().size() > 1) {
			// handle multiple root goals
			List<Goal> goals =  goalsChangeRequest.getGoalChange().stream()
					.filter((change) -> change.getOp() == GoalChangeOp.ADD)
					.map((change) -> change.getGoal()).collect(Collectors.toList());
			
			
			VE dvm = VEBuilder.create()
					.virtualDependency()
					.build();
			
			Alternative virtualAlt = AlternativeBuilder.create()
					.forVe(dvm)
					.withDependencies(goals)
					.build();
			
			dvm.getAlts().add(virtualAlt);
			updater.resolveVE(dvm);
			goalDmanager.setRootDame(dvm);
			// rootDame.setChosenAlt(virualAlt);
		} else {
			GoalChange change = goalsChangeRequest.getGoalChange().get(0);
			
			List<Dependency> query = RepoQueryBuilder.create()
					.queryFor(change.getGoal())
					.build();
				
			VE rootDame = updater.resolveDepenencies(query).get(0);
			goalDmanager.setRootDame(rootDame);
		}
	}

}
