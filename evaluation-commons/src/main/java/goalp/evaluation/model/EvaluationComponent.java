package goalp.evaluation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationComponent {
	
	private Map<String, Number> factors = new HashMap<>();
	
	private String responseVariable;
	
	private Number responseValue;
	
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
	
	public EvaluationComponent blankCopy(){
		EvaluationComponent clone = new EvaluationComponent();
		for(String key:this.factors.keySet()){
			clone.factors.put(key, null);
		}
		clone.responseVariable = this.responseVariable;
		return clone;
	}

}
