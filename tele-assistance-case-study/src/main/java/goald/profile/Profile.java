package goald.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {
	
	private Map<String, Float> ratioFailureMap;
	
	private Map<String, List<Variable>> ratioVariableMap;

	public Float getForBundle(String bundleIdentification) {
		return ratioFailureMap.get(bundleIdentification);
	}

	/**
	 * @return the rateFailureMap
	 */
	public Map<String, Float> getRateFailureMap() {
		if(this.ratioFailureMap == null) {
			this.ratioFailureMap = new HashMap<>();
		}
		return ratioFailureMap;
	}

	/**
	 * @return the ratioVariableMap
	 */
	public Map<String, List<Variable>> getRatioVariableMap() {
		if(this.ratioVariableMap == null) {
			this.ratioVariableMap = new HashMap<>();
		}
		return ratioVariableMap;
	}
	
	public List<Variable> getVariableList(String serviceName){
		List<Variable> list = getRatioVariableMap().get(serviceName);
		if( list!=null ) {
			return list;
		}
		
		list = new ArrayList<>();
		getRatioVariableMap().put(serviceName, list);
		return list;
	}
}
