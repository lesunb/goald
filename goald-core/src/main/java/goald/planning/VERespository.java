package goald.planning;

import java.util.ArrayList;
import java.util.List;

import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.Dependency;
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
		for(Dependency dependency:dependencies) {
			Bundle def = this.repo.queryForDefinition(dependency);
			List<Bundle> impls = this.repo.queryForImplementations(dependency);
			if(def == null || impls == null || impls.isEmpty()) {
				return null;
			}
			
			VE ve = new VE();
			ve.setDefinition(def);
			ve.setSatisfy(dependency);
			
			for(Bundle impl:impls) {
				Alternative alt = AlternativeBuilder.create()
						.forVe(ve)
						.from(def, impl)
						.build();
				
				ve.getAlts().add(alt);
			}
			ves.add(ve);
		}
		return ves;
	}
}
