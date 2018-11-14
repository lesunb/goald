package goald.evaluation.tas;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.Evaluation;
import goald.eval.exec.ScenarioBasedExperimentExecutior;
import goald.evaluation.EvaluationBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.planning.DameRespository;
import goald.repository.IRepository;

public class TeleAssistanceStudyCaseScenarios extends ScenarioBasedExperimentExecutior {
	
	@Inject
	Logger log;
	
	public void setup() {
		repo = new DameRespository(getRepo());
	}

	public List<Evaluation>  caseStudy() {
		List<Evaluation> evaluations = new ArrayList<>();
		Evaluation baseEvaluation = EvaluationBuilder.create()
				.setConstant("resultFileName", "fsa_dataset")
				.build();
		/* 
		 * Study case scenarios. Each one define a different set of contexts. 
		 * For each one the deployment will be planned
		 */
		scenarioRepetitions(1, 1, (ctxEvaluatorBuilder)->{
			ctxEvaluatorBuilder.with(
					"internet-connection", "drug-being-administered");
			},
			(qualityWeights) -> {
//				qualityWeights.put("precision", 1);
//				qualityWeights.put("responseTime", 1);
//				qualityWeights.put("storegedSize", 1);
//				qualityWeights.put("usability", 1);
			},
			(goalsChangeBuilding)->{
				goalsChangeBuilding.addGoal("ProvideHealthSupport");
			},
			(changesList) -> {
				changesList.add(ContextChangeBuilder.create()
						.remove("internet-connection").build());
				changesList.add(ContextChangeBuilder.create()
						.add("internet-connection").build());
		}, evaluations, baseEvaluation);

		return evaluations;
	}
	
	protected IRepository getRepo(){
		return TASRepository.getRepo();
	}
}
