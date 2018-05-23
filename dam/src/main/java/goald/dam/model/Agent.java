package goald.dam.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent {

	private List<ContextCondition> actualCtx;
	
	private Dame rootDame;
		
	private Map<String, Integer> weightMap;

	public List<ContextCondition> getActualCtx() {
		return actualCtx;
	}

	public void setActualCtx(List<ContextCondition> actualCtx) {
		this.actualCtx = actualCtx;
	}

	public Dame getRootDame() {
		return rootDame;
	}

	public void setRootDame(Dame rootDame) {
		this.rootDame = rootDame;
	}
	
	public void setWeight(String label, int value) {
		if(weightMap == null) {
			weightMap = new HashMap<>();
		}
		weightMap.put(label, value);
	}

	public Integer getQualityWeight(String label) {
		return weightMap.get(label);
	}
}
