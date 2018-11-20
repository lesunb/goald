package goald.planning;

import java.util.Set;
import java.util.Vector;

import goald.model.GoalDManager;
import goald.model.ContextChange;
import goald.model.VE;

public class ContextChangeHandler {
	
	DamUpdater updater;
	
	GoalDManager agent;
	
	public ContextChangeHandler(DameRespository repo, GoalDManager agent) {
		this.agent = agent;
		this.updater = new DamUpdater(repo, agent);
	}

	public Boolean handle(ContextChange change) {
		this.agent.getActualCtx().update(change);
		Set<VE> affectedDames = this.agent.getCtxVEMap().get(change.getLabel());
			
		if(affectedDames == null || affectedDames.isEmpty()) {
			return true;
		}

		boolean result = true;

		Vector<VE> affectedDamesVector = new Vector<>();
		affectedDamesVector.addAll(affectedDames);
		
		for(VE affected:affectedDamesVector) {
			boolean previousStatus = affected.getIsAchievable();
			VE currentPoint = this.updater.resolveDame(affected);
			
			//changed and parent is not null
			while(previousStatus != currentPoint.getIsAchievable().booleanValue()
					&& currentPoint.getParentAlt() != null){
				
				currentPoint = currentPoint.getParentAlt().getParentDame();
				previousStatus = currentPoint.getIsAchievable();
				// should reevaluate parents
				currentPoint = this.updater.resolveDame(currentPoint);
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
