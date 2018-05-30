package goalp.exputil;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.Evaluation;
import goald.eval.exec.Execution;
import goalp.systems.DeploymentPlanningResult;

public class EchoService {

	@Inject
	Logger log;
	
	public void it(Execution exec, DeploymentPlanningResult resultPlan) {
		it(exec.getEvaluation(), resultPlan);
	}

	public void it(Evaluation evaluation, DeploymentPlanningResult resultPlan) {
		log.info("############################ Exec Result");
		log.info("# Factors:" + evaluation.getFactors());
		log.info("# Response:" + evaluation.getResponseVariable());
		log.info("# Plan Size:" + resultPlan.getPlan().getSelectedArtifacts().size());
		log.info("#############################################");
	}

}
