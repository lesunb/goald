package goald.behaviour;

import java.util.HashMap;
import java.util.Map;

public class Profile {
	
	private Map<String, Float> rateFailureMap;

	public Float getForBundle(String bundleIdentification) {
		return rateFailureMap.get(bundleIdentification);
	}

	/**
	 * @return the rateFailureMap
	 */
	public Map<String, Float> getRateFailureMap() {
		if(this.rateFailureMap == null) {
			this.rateFailureMap = new HashMap<>();
		}
		return rateFailureMap;
	}

}
