package goald.model.util;

import java.util.List;

import goald.model.Goal;
import goald.model.GoalsChangeRequest;
import goald.model.GoalsChangeRequest.GoalChangeOp;

public class GoalsChangeRequestBuilder {

	private GoalsChangeRequest request;

	private GoalsChangeRequestBuilder() {
		this.request = new GoalsChangeRequest();
	}

	public static GoalsChangeRequestBuilder create() {
		return new GoalsChangeRequestBuilder();
	}

	public GoalsChangeRequest build() {
		GoalsChangeRequest built = this.request;
		this.request = null;
		return built;
	}

	public GoalsChangeRequestBuilder addGoal(String goalIdentification) {
		this.request.getGoalChange().add((this.request.new GoalChange(GoalChangeOp.ADD, 
				new Goal(goalIdentification))));
		return this;
	}

	public GoalsChangeRequestBuilder addGoals(List<String> goalIdentifications) {
		goalIdentifications.forEach(goalIdentification ->{
			addGoal(goalIdentification);			
		});
		return this;
	}
}
