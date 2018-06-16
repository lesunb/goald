package goald.eval.exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import goald.evaluation.ExperimentTimerImpl.Split;

public class Evaluation {
	
	private Map<String, Number> factors = new HashMap<>();
	
	private String responseVariable;
	
	private Number responseValue;
	
	// for experiments with many observations
	private List<Long> observations;
	
	private List<Split> times;
	
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
	
	public void addObservation(Long observation) {
		if(this.observations == null) {
			this.observations = new ArrayList<>();
		}
		this.observations.add(observation);
	}
	
	public Evaluation blankCopy(){
		Evaluation clone = new Evaluation();
		for(String key:this.factors.keySet()){
			clone.factors.put(key, null);
		}
		clone.responseVariable = this.responseVariable;
		return clone;
	}

	public void addMesures(List<Split> times) {
		this.times = times;
	}

}
