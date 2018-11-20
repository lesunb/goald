package goald.planning;

import java.util.ArrayList;
import java.util.List;

import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.Dependency;
import goald.model.Goal;
import goald.model.VE;
import goald.model.util.AlternativeBuilder;
import goald.repository.IRepository;

public class VERespository {
	
	IRepository repo;
	
	public VERespository(IRepository repo) {
		this.repo = repo;
	}
	
	public List<VE> queryForDependencies(Alternative alt){
		List<VE> ves = this.queryRepo(alt.getDependencies());
		if(ves == null) {
			return null;
		}
		ves.forEach(ve -> {ve.setParentAlt(alt);});
		return ves;
	}
	
	public List<VE> queryRepo(List<Dependency> dependencies){
		List<VE> ves = new ArrayList<>();
		for(Dependency depenency:dependencies) {
			Bundle def = this.repo.queryForDefinition(depenency);
			List<Bundle> impls = this.repo.queryForImplementations(depenency);
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
			ves.add(dame);
		}
		return ves;
	}
}
