package goald.evaluation.response;

import java.util.HashMap;
import java.util.Map;

import goald.eval.exec.ExecResult;
import goald.evaluation.EvaluationAbstract;
import goald.evaluation.Measure;
import goald.exputil.ExperimentTimer;

public class ResponseEvaluation extends EvaluationAbstract<Measure> {
	
	private String responseVariable;
	
	private Number responseValue;
	
	private ExecResult result;
	
	private ExperimentTimer timer;
	
	public String getResponseVariable() {
		return responseVariable;
	}

	public void setResponseVariable(String responseVariable) {
		this.responseVariable = responseVariable;
	}

	public Number getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(Number responseValue) {
		this.responseValue = responseValue;
	}
	
	
	public ResponseEvaluation blankCopy(){
		ResponseEvaluation clone = new ResponseEvaluation();
		for(String key:this.factors.keySet()){
			clone.factors.put(key, null);
		}
		for(String key:this.constant.keySet()){
			clone.constant.put(key, this.constant.get(key));
		}
		clone.responseVariable = this.responseVariable;
		clone.timer = this.timer;
		return clone;
	}

	public void split(Integer execIndex, String label) {
		Long value = getTimer().split(label);
		Measure mesure = new Measure(label, value);
		getMeasures(execIndex).add(mesure);
		this.orderedMeasuresKeysSet.add(label);
	}
		
	public void setResult(ExecResult result) {
		this.result = result;
	}
	
	public ExecResult getResult() {
		return this.result;
	}
	
	public ExperimentTimer getTimer() {
		if(this.timer == null) {
			this.timer = new SplitTimer();
			this.timer.begin();
		}
		return this.timer;
	}
	
	public Map<String, String> getMeasuresMap(Integer index) {
		Map<String, String> measureMap  = new HashMap<>();
		this.getIndexedMeasures().get(index).forEach(measure -> {
			measureMap.put(measure.getLabel(), measure.getValue());
		});
		return measureMap;
	}
	
	public void begin() {
		this.getTimer().begin();
	}
	
	public void setTimer(ExperimentTimer timer) {
		this.timer = timer;
	}

	@Override
	public String getResultToPrint() {
		// TODO Auto-generated method stub
		return null;
	}
}
