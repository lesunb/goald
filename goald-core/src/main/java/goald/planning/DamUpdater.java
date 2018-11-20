package goald.planning;

import java.util.List;

import goald.model.GoalDManager;
import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.CtxEvaluator;
import goald.model.VE;
import goald.model.Goal;
import goald.model.QualityParameter;

public class DamUpdater {
	
	GoalDManager agent;
	DameRespository repo;
	
	public DamUpdater(DameRespository repo, GoalDManager agent) {
		this.repo = repo;
		this.agent = agent;
	}
	
	public List<VE> resolveGoals(List<Goal> goals) {
		List<VE> dames = repo.queryRepo(goals);
		
		for(VE dame:dames) {
			resolveDame(dame);
		}
		return dames;
	}
	
	public VE resolveDame(VE dame) {
		CtxEvaluator ctx = this.agent.getActualCtx();
		dame.setChosenAlt(null);
		Alternative bestAlternative = null;
		
		for(Alternative alt: dame.getAlts()) {
			agent.getCtxVEMap().add(alt.getCtxReq(), dame);
			if(!ctx.check(alt.getCtxReq())) {
				//context not satisfied, can't apply this alternative
				alt.setResolved(false);
			}else {
				// context satisfied, resolve dependencies
				boolean resolved = true;
				if(!alt.getDependencyGoals().isEmpty()) {
					List<VE> depDames = repo.queryForDependencies(alt);
					alt.setListDepDame(depDames);
					
					if(depDames == null) {
						throw new RuntimeException("dependency goals not resolved" + alt.getDependencyGoals());
					}
					
					for(VE dependency: depDames) {
						VE result = resolveDame(dependency);
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
		boolean isAchievable = bestAlternative != null;
		dame.setIsAchievable(isAchievable);	
		return dame;
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
