package goald.evaluation.tas;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.TickProducer;
import goald.eval.exec.TimelineBasedExperimentExecutor;
import goald.evaluation.Evaluation;
import goald.evaluation.TickerTimer;
import goald.evaluation.timeline.TimelineEvaluation;
import goald.evaluation.timeline.TimelineEvaluationBuilder;
import goald.planning.DameRespository;
import goald.repository.IRepository;


public class TeleAssistanceStudyCaseScenarios extends TimelineBasedExperimentExecutor {
	
	@Inject
	Logger log;
	
	public void setup() {
		repo = new DameRespository(getRepo());
	}

	public List<TimelineEvaluation>  caseStudy() {
		List<TimelineEvaluation> evaluations = new ArrayList<>();
		TimelineEvaluation baseEvaluation = TimelineEvaluationBuilder.create()
				.setConstant("resultFileName", "tsa_dataset")
				.setTimer(TickerTimer.create())
				.build();
		/* 
		 * Study case scenarios. Each one define a different set of contexts. 
		 * For each one the deployment will be planned
		 */
		scenarioRepetitions(1, 1, 
			TickProducer.create(1000l,0l,30*1000l),
			(ctxEvaluatorBuilder)-> ctxEvaluatorBuilder
				.with(
					"internet-connection", 
					"drug-being-administered")
			,
			(qualityWeightsMap) -> {
				qualityWeightsMap.put("precision", 2);
				qualityWeightsMap.put("responseTime", 1);
				qualityWeightsMap.put("storegedSize", 1);
				qualityWeightsMap.put("usability", 1);
			},
			(goalsChangeBuilding)-> goalsChangeBuilding
				.addGoal("ProvideHealthSupport")
			
			,
			(ctxMonitorBuilding) -> ctxMonitorBuilding
				.add(100l, "internet-connection")
				.remove(10*1000l, "internet-connection")
			
			, evaluations, baseEvaluation);

		return evaluations;
	}
	
	protected IRepository getRepo(){
		return TASRepository.getRepo();
	}
}
