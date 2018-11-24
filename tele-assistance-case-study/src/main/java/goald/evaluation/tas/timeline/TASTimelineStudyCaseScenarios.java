package goald.evaluation.tas.timeline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.ExperimentExecutorTimelineBased;
import goald.evaluation.tas.TASRepository;
import goald.evaluation.timeline.TickProducer;
import goald.evaluation.timeline.TimelineEvaluation;
import goald.evaluation.timeline.TimelineTimer;
import goald.planning.VERespository;
import goald.repository.IRepository;


public class TASTimelineStudyCaseScenarios extends ExperimentExecutorTimelineBased {
	
	@Inject
	Logger log;
	
	@Inject
	TimelineEvaluation baseEvaluation;
	
	public void setup() {
		repo = new VERespository(getRepo());
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
				TickProducer.create(100l,0l,12000l),
				(ctxEvaluatorBuilder)-> ctxEvaluatorBuilder
					.with(
						"internet-connection")
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
					.add(2000l, "!battery-is-low")
					.rem(3000l, "internet-connection")
					.add(4000l, "internet-connection")
					.add(6000l, "!patient-is-ok")
					.add(7000l, "doctor-is-present")
					.add(8000l, "drug-is-available")
					.rem(1000l, "!patient-is-ok")
					.rem(11000l, "!battery-is-low")
					.rem(12000l, "doctor-is-present")
				, evaluations, baseEvaluation);

		return evaluations;
	}
	
	
	
	protected IRepository getRepo(){
		return TASRepository.getRepo();
	}
}
