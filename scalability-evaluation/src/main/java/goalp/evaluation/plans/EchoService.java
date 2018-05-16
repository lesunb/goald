package goalp.evaluation.plans;

import javax.inject.Inject;

import org.slf4j.Logger;

import goalp.evaluation.model.Execution;
import goalp.evaluation.model.ExperimentSetup;
import goalp.systems.DeploymentPlanningResult;

public class EchoService {

	@Inject
	Logger log;
	
	public void it(ExperimentSetup expSetup) {
		log.info("############################ Experiment Setup");
		log.info("# Repository of Size:" + expSetup.getRepository().getSize());
		log.info("# Nº RootGoal:" + expSetup.getRootGoals().size());
		log.info("# Agent Contexts:" + expSetup.getAgent().getContext());
		log.info("#############################################");
	}

	public void it(Execution exec, DeploymentPlanningResult resultPlan) {
		log.info("############################ Exec Result");
		log.info("# Factors:" + exec.getEvaluation().getFactors());
		log.info("# Response:" + exec.getEvaluation().getResponseVariable());
		log.info("# Plan Size:" + resultPlan.getPlan().getSelectedArtifacts().size());
		log.info("#############################################");
	}

}
