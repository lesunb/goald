package goald.evaluation.tas;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.ExperimentExecutorTimelineBased;
import goald.evaluation.timeline.TickProducer;
import goald.evaluation.timeline.TimelineEvaluation;
import goald.evaluation.timeline.TimelineTimer;
import goald.planning.DameRespository;
import goald.repository.IRepository;


public class TASTimelineStudyCaseScenarios extends ExperimentExecutorTimelineBased {
	
	@Inject
	Logger log;
	
	@Inject
	TimelineEvaluation baseEvaluation;
	
	public void setup() {
		repo = new DameRespository(getRepo());
	}

	public List<TimelineEvaluation>  caseStudy() {
		List<TimelineEvaluation> evaluations = new ArrayList<>();
		baseEvaluation.getConstants().put("resultFileName", "tsa_dataset");
		baseEvaluation.setTimer(TimelineTimer.create());
		
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
				.rem(10*1000l, "internet-connection")
			, evaluations, baseEvaluation);
		
		scenarioRepetitions(1, 2, 
				TickProducer.create(100l,0l,30*1000l),
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
					.rem(1*1000l, "internet-connection")
					.add(2*1000l, "internet-connection")
					.rem(3*1000l, "internet-connection")
					.add(4*1000l, "internet-connection")
					.rem(5*1000l, "internet-connection")
					.add(6*1000l, "internet-connection")
					.rem(7*1000l, "internet-connection")
				, evaluations, baseEvaluation);

		return evaluations;
	}
	
	
	
	protected IRepository getRepo(){
		return TASRepository.getRepo();
	}
}
