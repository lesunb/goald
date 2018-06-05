package goald.model;

import java.util.HashMap;
import java.util.Map;



public class Agent {

	private CtxEvaluator actualCtx;
	
	private Dame rootDame;
		
	private Map<String, Integer> weightMap;
	
	private CtxDameMap ctxDamesMap;
	
	private Deployment deployment;

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

	public CtxDameMap getCtxDamesMap() {
		if(ctxDamesMap == null) {
			ctxDamesMap = new CtxDameMap();
		}
		return ctxDamesMap;
	}

	public void setCtxDamesMap(CtxDameMap ctxDamesMap) {
		this.ctxDamesMap = ctxDamesMap;
	}

	public Deployment getDeployment() {
		if(deployment == null) {
			deployment = new Deployment();
		}
		return deployment;
	}

	public void setDeployment(Deployment deployment) {
		this.deployment = deployment;
	}

	@Override
	public String toString() {
		return "Agent [actualCtx=" + actualCtx + ", rootDame=" + rootDame + ", weightMap=" + weightMap
				+ ", ctxDamesMap=" + ctxDamesMap + ", deployment=" + deployment + "]";
	}

}
