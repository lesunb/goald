package goald.dam.planning;

import java.util.ArrayList;
import java.util.List;

import goald.dam.model.Agent;
import goald.dam.model.Alternative;
import goald.dam.model.Bundle;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.util.AlternativeBuilder;
import goalp.repository.IRepository;

public class UpdateDame {
	
	IRepository repo;
	Agent agent;
	
	public UpdateDame(IRepository repo, Agent agent) {
		this.repo = repo;
		this.agent = agent;
	}

	public boolean resolveAlt(List<String> ctx, Alternative alt) {
		
		if(!checkCtx(ctx, alt.getCtxReq())) {
			return false;
		}
		
		if(alt.getDependencyGoals() == null) {
			return true;
		}
		
		alt.setListDepDame(queryRepo(alt.getDependencyGoals()));
		
		
		return false;
	}
	
	public boolean checkCtx(List<String> actualCtx, List<String> reqCtx) {
		for(String ctx:reqCtx) {
			if(!actualCtx.contains(ctx)) {
				return false;
			}
		}
		
		return true;
	}
	
	public List<Dame> queryRepo(List<Goal> goals){
		List<Dame> dames = new ArrayList<>();
		for(Goal goal:goals) {
			Bundle def = this.repo.queryForDefinition(goal);
			List<Bundle> impls = this.repo.queryForImplementations(goal);
			if(def == null || impls == null || impls.isEmpty()) {
				return null;
			}
			
			Dame dame = new Dame();
			dame.setDefinition(def);
			
			for(Bundle impl:impls) {
				Alternative alt = AlternativeBuilder.create()
						//.addCtxReq(impl.getConditions())
						.build();
				
				dame.getAlts().add(alt);
			}
			dames.add(dame);
		}
		return dames;
	}
}
