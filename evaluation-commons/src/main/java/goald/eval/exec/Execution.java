package goald.eval.exec;

import goald.eval.spec.ExecSpec;

public class Execution {

	private ExecSpec spec;

	private Evaluation evaluation;
	
	public ExecSpec getSpecification() {
		return spec;
	}
	
	public void setSpecification(ExecSpec spec){
		this.spec = spec;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
}