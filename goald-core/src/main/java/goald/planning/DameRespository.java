package goald.planning;

import java.util.ArrayList;
import java.util.List;

import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.VE;
import goald.model.Goal;
import goald.model.util.AlternativeBuilder;
import goald.repository.IRepository;

public class DameRespository {
	
	IRepository repo;
	
	public DameRespository(IRepository repo) {
		this.repo = repo;
	}
	
	public List<VE> queryForDependencies(Alternative alt){
		List<VE> dames = this.queryRepo(alt.getDependencyGoals());
		if(dames == null) {
			return null;
		}
		dames.forEach(dame -> {dame.setParentAlt(alt);});
		return dames;
	}
	
	public List<VE> queryRepo(List<Goal> goals){
		List<VE> dames = new ArrayList<>();
		for(Goal goal:goals) {
			Bundle def = this.repo.queryForDefinition(goal);
			List<Bundle> impls = this.repo.queryForImplementations(goal);
			if(def == null || impls == null || impls.isEmpty()) {
				return null;
			}
			
			VE dame = new VE();
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
