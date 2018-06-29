package goald.evaluation;

import goald.eval.exec.Evaluation;
import goald.eval.spec.Experiment;

public class EvaluationBuilder {
	
	protected Evaluation evaluation;
	
	protected Experiment experiment;
	
	protected EvaluationBuilder(){
		this.evaluation = new Evaluation();
	}
	
	public static EvaluationBuilder create(){
		return new EvaluationBuilder();
	}
	
	public Evaluation build(){
		Evaluation built = this.evaluation;
		this.evaluation = null;
		return built;
	}
}
