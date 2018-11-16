package goald.model;

import java.util.ArrayList;
import java.util.List;

public class GoalsChangeRequest extends Change  {
	public enum GoalChangeOp {
		ADD,
		REMOVE
	}
	
	private List<GoalChange> goalChanges;
	
	public class GoalChange {
		private GoalChangeOp op;
		private Goal goal;
		
		public GoalChange(GoalChangeOp op, Goal goal) {
			this.op = op;
			this.goal = goal;
		}
		
		public GoalChangeOp getOp() {
			return this.op;
		}
		
		public Goal getGoal() {
			return this.goal;
		}

		@Override
		public String toString() {
			return "GoalChange [" + op + ":" + goal + "]";
		}
		
	}

	public List<GoalChange> getGoalChange() {
		if(goalChanges == null) {
			goalChanges = new ArrayList<>();
		}
		return goalChanges;
	}

	@Override
	public String toString() {
		return "GoalsChangeRequest [goalChanges=" + goalChanges + "]";
	}
	
}
