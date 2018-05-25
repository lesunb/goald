package goald.dam.planning;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import goald.dam.model.Agent;
import goald.dam.model.Alternative;
import goald.dam.model.Bundle;
import goald.dam.model.CtxEvaluator;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.QualityParameter;

public class DamUpdater {
	
	Agent agent;
	DameRespository repo;
	
	public DamUpdater(DameRespository repo, Agent agent) {
		this.repo = repo;
		this.agent = agent;
	}
	
	public List<Dame> resolveGoals(List<Goal> goals) {
		List<Dame> dames = repo.queryRepo(goals);
		
		for(Dame dame:dames) {
			resolveDame(dame);
		}
		return dames;
	}
	
	public boolean resolveDame(Dame dame) {
		CtxEvaluator ctx = this.agent.getActualCtx();
		dame.setChosenAlt(null);
		boolean result = false;
		
		List<Alternative> orderedAlts = orderAlt(dame.getAlts(), dame.getDefinition());
		Iterator<Alternative> alts = orderedAlts.iterator();
		
		while(!result && alts.hasNext() ) {
				Alternative candidate = alts.next();
				agent.getCtxDamesMap().add(candidate.getCtxReq(), dame);
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

	public boolean resolveAlt(CtxEvaluator ctx, Alternative alt) {
		
		if(!ctx.check(alt.getCtxReq())) {
			// invalid context
			return false;
		}
		
		if(alt.getDependencyGoals().size() == 0) {
			// valid context without dependencies
			return true;
		}else {
			/// valid context with dependencies, resolve them all.
			// check all dependencies
			Collection<Dame> dames = repo.queryRepo(alt.getDependencyGoals());
			if(dames == null) { // for any goal, has no alternative
				return false;
			}
			for(Dame dame: dames) {
				if(!resolveDame(dame)) {
					return false;
				}else {
					alt.getListDepDame().add(dame);
				}
			}
			return true;
		}
	}

	public List<Alternative> orderAlt(List<Alternative> alts, Bundle definition) {
		
		for(Alternative alt: alts) {
			int quality = 0;
			for(QualityParameter param: alt.getImpl().getQualityParams()) {
				quality += param.getValue() * agent.getQualityWeight(param.getLabel());
			}
			alt.setQuality(quality);
		}
		
		alts.sort( 
				(alt1, alt2) 
				-> alt2.getQuality().compareTo(alt1.getQuality()));
		
		return alts;
	}
}
