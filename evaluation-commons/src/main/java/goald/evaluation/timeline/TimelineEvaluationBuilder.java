package goald.evaluation.timeline;

import goald.eval.spec.Experiment;
import goald.evaluation.TickerTimer;

public class TimelineEvaluationBuilder {
	
	protected TimelineEvaluation evaluation;
	
	protected Experiment experiment;
	
	protected TimelineEvaluationBuilder(){
		this.evaluation = new TimelineEvaluation();
	}
	
	public static TimelineEvaluationBuilder create(){
		return new TimelineEvaluationBuilder();
	}
	
	public TimelineEvaluationBuilder setConstant(String label, Object value) {
		this.evaluation.getConstants().put(label, value);
		return this;
	}

	public TimelineEvaluationBuilder setTimer(TickerTimer tickerTimer) {
		this.evaluation.setTimer(tickerTimer);
		return this;
	}
	
	public TimelineEvaluation build(){
		TimelineEvaluation built = this.evaluation;
		this.evaluation = null;
		return built;
	}

}
