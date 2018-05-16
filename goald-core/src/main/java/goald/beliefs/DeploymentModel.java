package goald.beliefs;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Component;

import goald.beliefs.model.DeploymentUnit;

public class DeploymentModel extends Component {

	List<DeploymentUnit> firstLevelDeploymentUnits;
	
	public List<DeploymentUnit> getFistLevelDeploymentUnits() {
		if( this.firstLevelDeploymentUnits == null) {
			this.firstLevelDeploymentUnits = new ArrayList<>();
		}
		return this.firstLevelDeploymentUnits;
	}	

}
