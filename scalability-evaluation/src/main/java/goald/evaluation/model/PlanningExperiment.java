package goald.evaluation.model;

import goald.eval.spec.Experiment;

public class PlanningExperiment extends Experiment{
	
	public String name;
	
	private RepoSpec repoSpec;

	public RepoSpec getRepoSpec() {
		if(repoSpec == null){
			repoSpec = new RepoSpec();
		}
		return repoSpec;
	}

	

}
