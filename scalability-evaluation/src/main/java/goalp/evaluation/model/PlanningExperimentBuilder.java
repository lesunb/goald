package goalp.evaluation.model;

public class PlanningExperimentBuilder extends ExperimentBuilder {
	
	protected PlanningExperiment getExperiment(){
		return (PlanningExperiment) this.experiment;
	}
	
	protected PlanningExperimentBuilder(){
		this.experiment = new PlanningExperiment();
	}
	
	public static PlanningExperimentBuilder create(){
		return new PlanningExperimentBuilder();
	}
	
	public PlanningExperiment build(){
		PlanningExperiment built = getExperiment();
		this.experiment = null;
		return built;
	}
	
	public PlanningExperimentBuilder putRepoSpec(String key, Object value) {
		getExperiment().getRepoSpec().put(key, value);
		return this;
	}
	
	public PlanningExperimentBuilder putRepoSpec(String key, int begin, int end) {
		int[] range = {begin, end};
		getExperiment().getRepoSpec().put(key, range);
		return this;
	}

}
