package goald.beliefs;

import com.artemis.Component;

import goald.beliefs.util.DeploymentUnit;
import goald.desires.model.Goal;

public class DeploymentModel extends Component {

	DeploymentUnit metaGoal;

	/**
	 * Modify the root meta goal adding a new goal dependency and 
	 * 
	 * @param goal
	 */
	protected void addFirstLevelGoal(Goal goal) {}
	

}
