package goald.dam.model;

import java.util.HashMap;
import java.util.Map;

import goald.dam.planning.CtxEvaluator;

public class Agent {

	private CtxEvaluator actualCtx;
	
	private Dame rootDame;
		
	private Map<String, Integer> weightMap;

	public CtxEvaluator getActualCtx() {
		return actualCtx;
	}

	public void setActualCtx(CtxEvaluator ctx) {
		this.actualCtx = ctx;
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
