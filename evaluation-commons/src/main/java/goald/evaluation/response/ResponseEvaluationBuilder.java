package goald.evaluation.response;

import goald.eval.spec.Experiment;
import goald.evaluation.TickerTimer;

public class ResponseEvaluationBuilder {
	
	protected ResponseEvaluation evaluation;
	
	protected Experiment experiment;
	
	protected ResponseEvaluationBuilder(){
		this.evaluation = new ResponseEvaluation();
	}
	
	public static ResponseEvaluationBuilder create(){
		return new ResponseEvaluationBuilder();
	}
	
	public ResponseEvaluationBuilder setConstant(String label, Object value) {
		this.evaluation.getConstants().put(label, value);
		return this;
	}

	public ResponseEvaluationBuilder setTimer(TickerTimer tickerTimer) {
		this.evaluation.setTimer(tickerTimer);
		return this;
	}
	
	public ResponseEvaluation build(){
		ResponseEvaluation built = this.evaluation;
		this.evaluation = null;
		return built;
	}

}
