package goald.dam.planning;

import java.util.ArrayList;
import java.util.List;

import goald.dam.model.Alternative;
import goald.dam.model.Bundle;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.util.AlternativeBuilder;
import goald.repository.IRepository;

public class DameRespository {
	
	IRepository repo;
	
	public DameRespository(IRepository repo) {
		this.repo = repo;
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
