package goald.planning;

import java.util.Set;
import java.util.Vector;

import goald.model.GoalDManager;
import goald.model.ContextChange;
import goald.model.VE;

public class ContextChangeHandler {
	
	DVMUpdater updater;
	
	GoalDManager agent;
	
	public ContextChangeHandler(VERespository repo, GoalDManager agent) {
		this.agent = agent;
		this.updater = new DVMUpdater(repo, agent);
	}

	public Boolean handle(ContextChange change) {
		this.agent.getActualCtx().update(change);
		Set<VE> affectedVes = this.agent.getCtxVEMap().get(change.getLabel());
			
		if(affectedVes == null || affectedVes.isEmpty()) {
			return true;
		}

		boolean result = true;

		Vector<VE> affectedVesVector = new Vector<>();
		affectedVesVector.addAll(affectedVes);
		
		for(VE affected:affectedVesVector) {
			boolean previousStatus = affected.getIsAchievable();
			VE currentPoint = this.updater.resolveVE(affected);
			
			//changed and parent is not null
			while(previousStatus != currentPoint.getIsAchievable().booleanValue()
					&& currentPoint.getParentAlt() != null){
				
				currentPoint = currentPoint.getParentAlt().getParentVE();
				previousStatus = currentPoint.getIsAchievable();
				// should reevaluate parents
				currentPoint = this.updater.resolveVE(currentPoint);
			}
			// got to root
			if(!currentPoint.getIsAchievable() &&
					currentPoint.getParentAlt() == null) {
				result = false;
				break;
			}
		}
		return result;
	}
	
}
