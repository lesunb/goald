package goald.dam.planning;

import goald.dam.model.Agent;
import goald.dam.model.ContextChange;
import goald.dam.model.Dame;

public class ContextChangeHandler {
	
	DamUpdater updater;
	
	Agent agent;
	
	public ContextChangeHandler(DameRespository repo, Agent agent) {
		this.agent = agent;
		this.updater = new DamUpdater(repo, agent);
	}

	public Boolean handle(ContextChange change) {
		
		boolean updated = this.agent.getActualCtx().update(change);
		
		if(!updated) { return false; }
		
		Dame rootDame = this.agent.getRootDame();
		
		Boolean result = updater.resolveDame(agent.getActualCtx(), rootDame);
		
		return result;
	}
	
}
