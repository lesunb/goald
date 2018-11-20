package goald.evaluation.tas;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.ExperimentExecutorScenarioBased;
import goald.evaluation.response.ResponseEvaluation;
import goald.evaluation.response.ResponseEvaluationBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.planning.VERespository;
import goald.repository.IRepository;

@Alternative
public class TASPerformanceStudyCaseScenarios extends ExperimentExecutorScenarioBased {
	
	@Inject
	Logger log;
	
	public void setup() {
		repo = new VERespository(getRepo());
	}

	public List<ResponseEvaluation>  caseStudy() {
		List<ResponseEvaluation> evaluations = new ArrayList<>();
		ResponseEvaluation baseEvaluation = ResponseEvaluationBuilder.create()
				.setConstant("resultFileName", "tsa_dataset")
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
