package goald.exputil;


import javax.inject.Inject;

import org.slf4j.Logger;

import goald.dam.model.DeploymentPlan;
import goald.eval.exec.Execution;
import goald.eval.exec.ExperimentSetup;

public class EchoService {

	@Inject
	Logger log;
	
	public void it(ExperimentSetup expSetup) {
		log.info("############################ Experiment Setup");
		log.info("# Repository of Size:" + expSetup.getRepository().getSize());
		log.info("# Nº RootGoal:" + expSetup.getRootGoals().size());
		//log.info("# Agent Contexts:" + expSetup.getAgent().getContext());
		log.info("#############################################");
	}

	public void it(Execution exec) {
		log.info("############################ Exec Result");
		log.info("# Factors:" + exec.getEvaluation().getFactors());
		log.info("# Response:" + exec.getEvaluation().getResponseVariable());
		//log.info("# Plan Size:" + exec.getEvaluation().getPlan().getSelectedArtifacts().size());
		log.info("#############################################");
	}
	
	public void it(DeploymentPlan adaptPlan) {
		log.info(adaptPlan.toString());
	}
}
