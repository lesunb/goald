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
	
	public List<VE> queryForDependency(Alternative alt, 
			Dependency dependency){
		List<VE> ves = this.queryRepo(dependency);
		if(ves == null) {
			return null;
		}
		ves.forEach(ve -> {ve.setParentAlt(alt);});
		return ves;
	}
	
	public List<VE> queryRepo(List<Dependency> dependencies){
		List<VE> ves = new ArrayList<>();
		for(Dependency dependency:dependencies) {
			Bundle def = this.repo.queryForDefinition(dependency.getIdentification());
			List<Bundle> impls = this.repo.queryForImplementations(dependency.getIdentification());
			if(def == null) {
				System.out.println("no def found for dep " + dependency);
				return null;
			}
			if( impls == null || impls.isEmpty()) {
				System.out.println("no impl found for dep " + dependency);
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
	
	public List<VE> queryRepo(Dependency dependency){
		List<VE> ves = new ArrayList<>();
		if(dependency.getIdentification() != null) {
			Bundle def = this.repo.queryForDefinition(dependency.getIdentification());
			List<Bundle> impls = this.repo.queryForImplementations(dependency.getIdentification());
			if(def == null) {
				System.out.println("no def found for dep " + dependency);
				return null;
			}
			if( impls == null || impls.isEmpty()) {
				System.out.println("no impl found for dep " + dependency);
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
			return ves;
		}else {
			throw new IllegalStateException();
		}
	}
}
