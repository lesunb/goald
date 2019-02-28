package goald.evaluation.timeline;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.ExecResult;
import goald.evaluation.EvaluationAbstract;

public class TimelineEvaluation extends EvaluationAbstract<TimelineMeasure> {
		
	private ExecResult result;
		
	private TimelineTimer timer;
	
	private Map<String, TimelineMeasure> whatIsOnMap = new HashMap<>();
	
	private Long endTime;
	
	@Inject
	private Logger logger;
	
	/**
	 * splitToogleOn/ splitToogleOff are used to create measures of type: label, start, end
	 * @param execIndex
	 * @param label
	 */
	public void splitToogleOn(Integer execIndex, String label, String type) {
		long timestamp = getTimer().getTimestamp();
		toogleOn(execIndex, label, type, timestamp);
	}
	
	public void splitToogleOn(Integer execIndex, String label) {
		this.splitToogleOn(execIndex, label, null);
	}
	
	public void toogleOn(Integer execIndex, String label, Long timestamp) {
		this.toogleOn(execIndex, label, null, timestamp);
	}
	
	public void toogleOn(Integer execIndex, String label, String type, Long timestamp) {
		if(whatIsOnMap.get(label) != null) {
			logger.warn("trying toogle on what is aready on " + label + " at " + timestamp);
		} else {
			TimelineMeasure mesure = new TimelineMeasure(label, type, timestamp, null);
			whatIsOnMap.put(label, mesure);
			getMeasures(execIndex).add(mesure);
			logger.trace(label + " on at " + timestamp);
		}
	}
	
	public void splitToogleOff(Integer execIndex, String label) {
		long timestamp = getTimer().getTimestamp();
		toogleOff(execIndex, label, timestamp);
	}
	
	public void toogleOff(Integer execIndex, String label, Long timestamp) {
		TimelineMeasure mesure = whatIsOnMap.get(label);
		if(mesure == null) {
			this.logger.warn("trying toogle off what is not on " + label + " at " + timestamp);
		} else {
			mesure.end = timestamp;
			logger.trace(label + " off at " + timestamp);
			whatIsOnMap.remove(label);
		}
	}
	
	public void endExec(long lastTime) {
		whatIsOnMap.values().forEach(measure ->{
			measure.end = lastTime;
		});
		endTime = lastTime;
		whatIsOnMap.clear();
	}	
	
	public void setResult(ExecResult result) {
		this.result = result;
	}
	
	public ExecResult getResult() {
		return this.result;
	}
	
	public TimelineTimer getTimer() {
		if(this.timer == null) {
			this.timer = TimelineTimer.create();
			this.timer.begin();
		}
		return this.timer;
	}
	
	public void begin() {
		this.timer.begin();
	}
	
	public void setTimer(TimelineTimer timer) {
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
		clone.logger = this.logger;
		return clone;
	}
	
	public Long getEndTime() {
		return this.endTime;
	}
	
	@Override
	public String getResultToPrint() {
		return "[method getResultToPrint not implemented]";
	}


}
