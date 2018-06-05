package goald.model.util;

import java.util.ArrayList;
import java.util.List;

import goald.model.Goal;

public class RepoQueryBuilder {
	
	protected List<Goal> goals;
	
	protected RepoQueryBuilder(){
		this.goals = new ArrayList<>();
	}
	
	public static RepoQueryBuilder create(){
		return new RepoQueryBuilder();
	}
	
	public List<Goal> build(){
		List<Goal> built = this.goals;
		this.goals = null;
		return built;
	}
	
	public RepoQueryBuilder queryFor(String identification){
		return queryFor(new Goal(identification));
	}

	public RepoQueryBuilder queryFor(Goal goal){
		this.goals.add(goal);
		return this;
	}
	
	public RepoQueryBuilder queryFor(String ...identifications){
		for(String identification: identifications) {
			queryFor(identification);
		}
		return this;
	}
}
