package goald.dam.planning;

import java.util.Iterator;
import java.util.List;

import goald.dam.model.Agent;
import goald.dam.model.Alternative;
import goald.dam.model.Bundle;
import goald.dam.model.Dame;

public class DamUpdater {
	
	Agent agent;
	DameRespository repo;
	
	public DamUpdater(DameRespository repo, Agent agent) {
		this.repo = repo;
		this.agent = agent;
	}

	public boolean resolveAlt(CtxEvaluator ctx, Alternative alt) {
		
		if(!ctx.check(alt.getCtxReq())) {
			// invalid context
			return false;
		}
		
		if(alt.getDependencyGoals() == null) {
			// valid context without dependencies
			return true;
		}else {
			/// valid context with dependencies, resolve them all.
			// check all dependencies
			for(Dame dame: repo.queryRepo(alt.getDependencyGoals())) {
				if(!resolveDame(ctx, dame)) {
					return false;
				}
			}
			return true;
		}
	}
	
	public boolean resolveDame(CtxEvaluator ctx, Dame dame) {
		
		boolean result = false;
		
		List<Alternative> orderedAlts = orderAlt(dame.getAlts(), dame.getDefinition());
		Iterator<Alternative> alts = orderedAlts.iterator();
		
		while(!result && alts.hasNext() ) {
				Alternative candidate = alts.next();
				result = resolveAlt(ctx, candidate);
				if(result) {
					candidate.setResolved(true);
					candidate.setCtxSatisfied(true);
					dame.setChosenAlt(candidate);
					break;
				}
		}
		return result;
	}
	

	public List<Alternative> orderAlt(List<Alternative> alts, Bundle definition) {
		// TODO Auto-generated method stub
		return alts;
	}

}
