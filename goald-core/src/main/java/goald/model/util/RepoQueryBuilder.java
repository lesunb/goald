package goald.model.util;

import java.util.ArrayList;
import java.util.List;

import goald.model.Dependency;
import goald.model.Goal;

public class RepoQueryBuilder {
	
	protected List<Dependency> dependencies;
	
	protected RepoQueryBuilder(){
		this.dependencies = new ArrayList<>();
	}
	
	public static RepoQueryBuilder create(){
		return new RepoQueryBuilder();
	}
	
	public List<Dependency> build(){
		List<Dependency> built = this.dependencies;
		this.dependencies = null;
		return built;
	}
	
	public RepoQueryBuilder queryFor(String identification){
		return queryFor(new Dependency(identification));
	}

	public RepoQueryBuilder queryFor(Dependency dependency){
		this.dependencies.add(dependency);
		return this;
	}
	
	public RepoQueryBuilder queryFor(Goal goal){
		this.dependencies.add(new Dependency(goal.getIdentication()));
		return this;
	}
	
	public RepoQueryBuilder queryFor(String ...identifications){
		for(String identification: identifications) {
			queryFor(identification);
		}
		return this;
	}
}
