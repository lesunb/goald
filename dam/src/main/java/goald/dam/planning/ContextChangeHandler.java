package goald.dam.planning;

import goald.dam.model.Agent;
import goald.dam.model.ContextChange;

public class ContextChangeHandler {
	
	DamUpdater updater;
	
	Agent agent;
	
	public ContextChangeHandler(DameRespository repo, Agent agent) {
		this.agent = agent;
		this.updater = new DamUpdater(repo, agent);
	}

	public Boolean handle(ContextChange change) {
		
		return null;
	}
	
}
