package goald.planning;

import java.util.List;

import goald.model.Agent;
import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.CtxEvaluator;
import goald.model.Dame;
import goald.model.Goal;
import goald.model.QualityParameter;

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
	
	public Dame resolveDame(Dame dame) {
		CtxEvaluator ctx = this.agent.getActualCtx();
		dame.setChosenAlt(null);
		Alternative bestAlternative = null;
		
		for(Alternative alt: dame.getAlts()) {
			agent.getCtxDamesMap().add(alt.getCtxReq(), dame);
			if(!ctx.check(alt.getCtxReq())) {
				//context not satisfied, can't apply this alternative
				alt.setResolved(false);
			}else {
				// context satisfied, resolve dependencies
				boolean resolved = true;
				if(!alt.getDependencyGoals().isEmpty()) {
					List<Dame> depDames = repo.queryRepo(alt.getDependencyGoals());
					alt.setListDepDame(depDames);
					
					if(depDames == null) {
						throw new RuntimeException("dependency goals not resolved" + alt.getDependencyGoals());
					}
					
					for(Dame dependency: depDames) {
						Dame result = resolveDame(dependency);
						if(!result.getIsAchievable()) {
							resolved = false;
							break;
						}
					}
				}
				alt.setResolved(resolved);
			}
			bestAlternative = getBestAlternative(bestAlternative, alt);
		}
		dame.setChosenAlt(bestAlternative);	
		dame.setIsAchievable(bestAlternative != null);
		
		return dame;
//		
//		
//		List<Alternative> orderedAlts = orderAlt(dame.getAlts(), dame.getDefinition());
//		Iterator<Alternative> alts = orderedAlts.iterator();
//		
//		while(!result && alts.hasNext() ) {
//				Alternative candidate = alts.next();
//				agent.getCtxDamesMap().add(candidate.getCtxReq(), dame);
//				result = resolveAlt(ctx, candidate);
//				if(result) {
//					candidate.setResolved(true);
//					candidate.setCtxSatisfied(true);
//					dame.setChosenAlt(candidate);
//					break;
//				}
//		}
//		return result;
	}

	private Alternative getBestAlternative(Alternative currentAlt, Alternative newAlt) {
		//check  if newalt is a viable alternative
		if(newAlt == null || !newAlt.getResolved()) {
			return currentAlt;
		}
		
		if(newAlt.getResolved()) {
			calcQuality(newAlt);
		}
		
		if(currentAlt == null || !currentAlt.getResolved() ) {
			// the new is the first resolved one
			return newAlt;
		}else {
			// both resolved, return the one with the best quality
			return currentAlt.getQuality() >= newAlt.getQuality() ? currentAlt: newAlt;
		}
	}
	
	private void calcQuality(Alternative alt) {
		int quality = 0;
		if(alt.getImpl() == null) {
			alt.setQuality(-1);
			return;
		}
		for(QualityParameter param: alt.getImpl().getQualityParams()) {
			Integer weight = agent.getQualityWeight(param.getLabel());
			if(weight == null) { 
				System.out.println("agent has no weight for label '"+ param.getLabel() + "'");
				weight = 1;
			}
			quality += param.getValue() * weight;
		}
		alt.setQuality(quality);
	}
//	
//	public boolean resolveAlt(CtxEvaluator ctx, Alternative alt) {
//		
//		if(!ctx.check(alt.getCtxReq())) {
//			// invalid context
//			return false;
//		}
//		
//		if(alt.getDependencyGoals().size() == 0) {
//			// valid context without dependencies
//			return true;
//		}else {
//			/// valid context with dependencies, resolve them all.
//			// check all dependencies
//			Collection<Dame> dames = repo.queryRepo(alt.getDependencyGoals());
//			if(dames == null) { // for any goal, has no alternative
//				return false;
//			}
//			for(Dame dame: dames) {
//				if(!resolveDame(dame)) {
//					return false;
//				}else {
//					alt.getListDepDame().add(dame);
//				}
//			}
//			return true;
//		}
//	}

	public List<Alternative> orderAlt(List<Alternative> alts, Bundle definition) {
		if(alts.size() == 1) {
			return alts;
		}
		
		for(Alternative alt: alts) {
			int quality = 0;
			for(QualityParameter param: alt.getImpl().getQualityParams()) {
				Integer weight = agent.getQualityWeight(param.getLabel());
				if(weight == null) { 
					System.out.println("agent has no weight for label '"+ param.getLabel() + "'");
					weight = 1;
				}
				quality += param.getValue() * weight;
			}
			alt.setQuality(quality);
		}
		
		alts.sort( 
				(alt1, alt2) 
				-> alt2.getQuality().compareTo(alt1.getQuality()));
		
		return alts;
	}
}
