package goald.eval.exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import goald.evaluation.Measure;
import goald.evaluation.Timer;

public class Evaluation {
	
	private Map<String, Number> factors = new HashMap<>();
	
	private String responseVariable;
	
	private Number responseValue;
	
	private ExecResult result;
	
	private Map<Integer, List<Measure>> indexedMeasures;
	
	private Timer timer;
	
	public Map<String, Number> getFactors() {
		if(factors == null){
			factors = new HashMap<String, Number>();
		}
		return factors;
	}
	
	public List<String> getFactorList(){
		List<String> list = new ArrayList<>();
		for(String factor:getFactors().keySet()){
			list.add(factor);
		}
		return list;
	}

	public void setFactors(Map<String, Number> factors) {
		this.factors = factors;
	}
	
	public void putFactor(String factor, Number value){
		getFactors().put(factor, value);
	}

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
	
	
	public Evaluation blankCopy(){
		Evaluation clone = new Evaluation();
		for(String key:this.factors.keySet()){
			clone.factors.put(key, null);
		}
		clone.responseVariable = this.responseVariable;
		return clone;
	}

	public void split(Integer execIndex, String label) {
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
	
	public Timer getTimer() {
		if(this.timer == null) {
			this.timer = new Timer();
			this.timer.begin();
		}
		return this.timer;
	}


	public List<Measure> getMeasures(Integer execIndex) {
		if(this.indexedMeasures == null) {
			this.indexedMeasures = new HashMap<>();
		}
		if(this.indexedMeasures.get(execIndex) == null) {
			this.indexedMeasures.put(execIndex, new ArrayList<>());
		}
		return this.indexedMeasures.get(execIndex);
	}

	public Map<Integer, List<Measure>> getIndexedMeasures() {
		return indexedMeasures;
	}
}
