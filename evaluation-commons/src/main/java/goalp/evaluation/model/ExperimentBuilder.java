package goalp.evaluation.model;

import java.util.List;

public class ExperimentBuilder {
	
	protected Experiment experiment;
	
	protected ExperimentBuilder(){
		this.experiment = new Experiment();
	}
	
	public static ExperimentBuilder create(){
		return new ExperimentBuilder();
	}
	
	public Experiment build(){
		Experiment built = this.experiment;
		this.experiment = null;
		return built;
	}
	
	public ExperimentBuilder addSpec(ExecSpec spec) {
		Execution exec = new Execution();
		
		//set exec spec
		exec.setSpecification(spec);
		
		//set exec eval with factors values from spec
		EvaluationComponent eval = this.experiment.getEvaluation().blankCopy();
		eval.getFactorList().forEach((factor)->{
			eval.putFactor(factor, spec.getInteger(factor));
		});
		exec.setEvaluation(eval);
		
		this.experiment.toExecute(exec);
		return this;
	}
	
	public ExperimentBuilder addSpecs(List<ExecSpec> createSpecsRangeSetter) {
		createSpecsRangeSetter.forEach((execSpec)->{
			addSpec(execSpec);
		});
		return this;
	}

	public ExperimentBuilder addFactor(String factor) {
		this.experiment.getEvaluation().putFactor(factor, null);
		return this;
	}
	
	public ExperimentBuilder addFactors(String[] factors) {
		for(String factor:factors){
			this.experiment.getEvaluation().putFactor(factor, null);			
		}
		return this;
	}

	public ExperimentBuilder setResponseVariable(String responseVariable) {
		this.experiment.getEvaluation().setResponseVariable(responseVariable);
		return this;
	}

	public ExperimentBuilder setName(String name) {
		this.experiment.name=name;
		return this;
	}
	
}
