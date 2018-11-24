package goald.evaluation.assertives;

import goald.model.CtxEvaluator;
import goald.model.Deployment;

public class Observation {

	protected CtxEvaluator ctx; 
	protected Deployment initialDeployment;
	protected Deployment finalDeployment;
	
	public Observation(CtxEvaluator ctx, 
			Deployment initialDeployment, Deployment finalDeploymen) {
		this.ctx = ctx;
		this.initialDeployment = initialDeployment;
		this.finalDeployment = finalDeploymen;
	}

	@Override
	public String toString() {
		return "Observation [ctx=" + ctx + ", initialDeployment=" + initialDeployment + ", finalDeployment="
				+ finalDeployment + "]";
	}

}
