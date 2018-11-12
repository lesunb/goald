package goald.planning;

import java.util.ArrayList;
import java.util.List;

import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.Dame;
import goald.model.Goal;
import goald.model.util.AlternativeBuilder;
import goald.repository.IRepository;

public class DameRespository {
	
	IRepository repo;
	
	public DameRespository(IRepository repo) {
		this.repo = repo;
	}
	
	public List<Dame> queryForDependencies(Alternative alt){
		List<Dame> dames = this.queryRepo(alt.getDependencyGoals());
		if(dames == null) {
			return null;
		}
		dames.forEach(dame -> {dame.setParentAlt(alt);});
		return dames;
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
						.forDame(dame)
						.from(def, impl)
						.build();
				
				dame.getAlts().add(alt);
			}
			dames.add(dame);
		}
		return dames;
	}
}
