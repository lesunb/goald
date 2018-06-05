package goald.planning;

import java.util.Set;

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
		
		for(Dame affected:affectedDames) {
			boolean thisResult = this.updater.resolveDame(affected);
			if(!thisResult) {
				if(affected.getParentAlt() == null) {
					result = false;
					break;
				}else {
					this.updater.resolveDame(affected.getParentAlt().getParentDame());
				}
			}
		}
		return result;
	}
	
}
