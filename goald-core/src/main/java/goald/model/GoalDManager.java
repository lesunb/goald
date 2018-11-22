package goald.model;

import java.util.HashMap;
import java.util.Map;



public class GoalDManager {

	private CtxEvaluator actualCtx;
	
	private VE dvm;
		
	private Map<String, Integer> weightMap;
	
	private CtxVEMap ctxVEMap;
	
	private Deployment deployment;

	public CtxEvaluator getActualCtx() {
		return actualCtx;
	}

	public void setActualCtx(CtxEvaluator ctx) {
		this.actualCtx = ctx;
	}

	public VE getRootDame() {
		return dvm;
	}

	public void setRootDame(VE rootDame) {
		this.dvm = rootDame;
	}
	
	public void setWeight(String label, int value) {
		if(weightMap == null) {
			weightMap = new HashMap<>();
		}
		weightMap.put(label, value);
	}
	
	public void setWeightMap(Map<String, Integer> weightMap) {
		this.weightMap = weightMap;
	}

	public Integer getQualityWeight(String label) {
		if(weightMap == null) {
			weightMap = new HashMap<>();
		}
		return weightMap.get(label);
	}

	public CtxVEMap getCtxVEMap() {
		if(ctxVEMap == null) {
			ctxVEMap = new CtxVEMap();
		}
		return ctxVEMap;
	}

	public void setCtxVEMap(CtxVEMap ctxVEMap) {
		this.ctxVEMap = ctxVEMap;
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
		return "GoalDManager [actualCtx=" + actualCtx + ", weightMap=" + weightMap
				+ ", ctxVEMap=" + ctxVEMap + ", deployment=" + deployment + ", dvm=" + dvm + "]";
	}

}
