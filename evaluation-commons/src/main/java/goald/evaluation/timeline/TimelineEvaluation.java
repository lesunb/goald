package goald.evaluation.timeline;

import goald.eval.exec.ExecResult;
import goald.evaluation.ClockTimer;
import goald.evaluation.Evaluation;
import goald.evaluation.EvaluationAbstract;
import goald.evaluation.Measure;
import goald.exputil.ExperimentTimer;

public class TimelineEvaluation extends EvaluationAbstract implements Evaluation {
		
	private ExecResult result;
		
	private ExperimentTimer timer;
	
	/**
	 * splitToogleOn/ splitToogleOff are used to create measures of type: label, start, end
	 * @param execIndex
	 * @param label
	 */
	public void splitToogleOn(Integer execIndex, String label) {
		Long value = getTimer().split(label);
		Measure mesure = new Measure(label, value);
		getMeasures(execIndex).add(mesure);
	}
	
	public void splitToogleOff(Integer execIndex, String label) {
		Long value = getTimer().split(label);
		Measure mesure = new Measure(label, value);
		getMeasures(execIndex).add(mesure);
	}
	
	public void setResult(ExecResult result) {
		this.result = result;
	}
	
	public ExecResult getResult() {
		return this.result;
	}
	
	public ExperimentTimer getTimer() {
		if(this.timer == null) {
			this.timer = new ClockTimer();
			this.timer.begin();
		}
		return this.timer;
	}
	
	public void begin() {
		this.timer.begin();
	}
	
	public void setTimer(ExperimentTimer timer) {
		this.timer = timer;
	}
	
	public TimelineEvaluation blankCopy(){
		TimelineEvaluation clone = new TimelineEvaluation();
		for(String key:this.factors.keySet()){
			clone.factors.put(key, null);
		}
		for(String key:this.constant.keySet()){
			clone.constant.put(key, this.constant.get(key));
		}
		clone.timer = this.timer.clone();
		return clone;
	}

	@Override
	public String getResultToPrint() {
		return "[method getResultToPrint not implemented]";
	}	
}
