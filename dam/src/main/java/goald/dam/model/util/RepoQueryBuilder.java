package goald.dam.model.util;

import java.util.ArrayList;
import java.util.List;

import goald.dam.model.ContextCondition;
import goald.dam.model.Goal;
import goald.dam.planning.CtxEvaluator;

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
		this.goals.add(new Goal(identification));
		return this;
	}
	
	public RepoQueryBuilder queryFor(String ...identifications){
		for(String identification: identifications) {
			queryFor(identification);
		}
		return this;
	}
}
