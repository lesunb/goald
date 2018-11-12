package goald.planning;

import java.util.Set;
import java.util.Vector;

import goald.model.Agent;
import goald.model.ContextChange;
import goald.model.Dame;



public class ContextChangeHandler {
	
	DamUpdater updater;
	
	Agent agent;
	
	public ContextChangeHandler(DameRespository repo, Agent agent) {
		this.agent = agent;
		this.updater = new DamUpdater(repo, agent);
	}

	public Boolean handle(ContextChange change) {
		this.agent.getActualCtx().update(change);
		Set<Dame> affectedDames = this.agent.getCtxDamesMap().get(change.getLabel());
		
		if(affectedDames == null) {
			return true;
		}
		
		boolean result = true;
		
		Vector<Dame> affectedDamesVector = new Vector<>();
		affectedDamesVector.addAll(affectedDames);
		
		for(Dame affected:affectedDamesVector) {
			boolean previousStatus = affected.getIsAchievable();
			Dame currentPoint = this.updater.resolveDame(affected);
		
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
