package goald.planning;

import java.util.List;

import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.CtxEvaluator;
import goald.model.Dependency;
import goald.model.DependencyModifier;
import goald.model.GoalDManager;
import goald.model.QualityParameter;
import goald.model.VE;

public class DVMUpdater {
	
	GoalDManager manager;
	VERespository repo;
	
	public DVMUpdater(VERespository repo, GoalDManager agent) {
		this.repo = repo;
		this.manager = agent;
	}
	
	public List<VE> resolveDepenencies(List<Dependency> dependencies) {
		List<VE> ves = repo.queryRepo(dependencies);
		
		for(VE ve:ves) {
			resolveVE(ve);
		}
		return ves;
	}
	
	public VE resolveVE(VE ve) {
		CtxEvaluator ctx = this.manager.getActualCtx();
		ve.setChosenAlt(null);
		
		if(!isDependencyCurrent(ve, ctx)) {
			//put in the map
			manager.getCtxVEMap().add(ve.getSatisfy()
					.getModifier().getConditions(), ve);
			ve.setIsAchievable(true);
			ve.setChosenAlt(null);
		}
		
		for(Alternative alt: ve.getAlts()) {
			manager.getCtxVEMap().add(alt.getCtxReq(), ve);
			if(!ctx.check(alt.getCtxReq())) {
				//context not satisfied, can't apply this alternative
				alt.setResolved(false);
			}else {
				// context satisfied, resolve dependencies
				boolean resolved = true;
				if(!alt.getDependencies().isEmpty()) {
					List<VE> depVElist = repo.queryForDependencies(alt);
					alt.setListDepVE(depVElist);
					
					if(depVElist == null) {
						throw new RuntimeException("dependency goals not resolved" + alt.getDependencies());
					}
					
					for(VE dependency: depVElist) {
						VE result = resolveVE(dependency);
						if(!result.isAchievable()) {
							resolved = false;
							break;
						}
					}
				}
				alt.setResolved(resolved);
			}
			selectBetterAlternativeSet(ve, alt);
		}
		boolean isSatifiable = checkAltValidity(ve, ctx);
		ve.setIsAchievable(isSatifiable);	
		return ve;
	}

	private void selectBetterAlternativeSet(VE ve, Alternative alt) {
		ve.setChosenAlt(getBestAlternative(ve.getChosenAlt(), alt));
	}
	
	private boolean checkAltValidity(VE ve, CtxEvaluator ctx) {
		if(ve.getChosenAlt() != null) {
			return true;
		} else {
			boolean validity; 
			switch(ve.getSatisfy().getModifier().getType()) {
				case COND:
					// if context cond do not hold, the alt can be null
					validity = !ctx.check(ve.getSatisfy().getModifier().getConditions());
					break;
				default:
					validity = false;
			}
			if(!validity) {
				System.out.println("can not satisfy: Dep "+ ve.getSatisfy() + " with ctx " + ctx.getCtxCollection());
			}
			return validity;
		}
	}
	
	private boolean isDependencyCurrent(VE ve, CtxEvaluator ctx){
		if(ve.getSatisfy().getModifier().getType() != DependencyModifier.Type.COND) {
			return true;
		}
		return ctx.check(ve.getSatisfy().getModifier().getConditions());
	}

	@Deprecated
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
			Integer weight = manager.getQualityWeight(param.getLabel());
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
				Integer weight = manager.getQualityWeight(param.getLabel());
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
