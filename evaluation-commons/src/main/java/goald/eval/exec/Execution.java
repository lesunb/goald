package goald.eval.exec;

import goald.eval.spec.ExecSpec;
import goald.evaluation.response.ResponseEvaluation;

public class Execution {

	private ExecSpec spec;

	private ResponseEvaluation evaluation;
	
	public ExecSpec getSpecification() {
		return spec;
	}
	
	public void setSpecification(ExecSpec spec){
		this.spec = spec;
	}

	public ResponseEvaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(ResponseEvaluation evaluation) {
		this.evaluation = evaluation;
	}
}