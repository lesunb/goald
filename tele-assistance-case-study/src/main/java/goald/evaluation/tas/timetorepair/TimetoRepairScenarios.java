package goald.evaluation.tas.timetorepair;

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


public class TimetoRepairScenarios extends ExperimentExecutorTimeToRepair {
	
	@Inject
	Logger log;
	
	@Inject
	TimelineEvaluation baseEvaluation;
	
	public void setup() {
		repo = new VERespository(getRepo());
	}

	public List<TimelineEvaluation>  caseStudy() {
		List<TimelineEvaluation> evaluations = new ArrayList<>();
		baseEvaluation.getConstants().put("resultFileName", "tsa_time_to_repair");
		baseEvaluation.setTimer(TimelineTimer.create());
		
		/* 
		 * Study case scenarios. Each one define a different set of contexts. 
		 * For each one the deployment will be planned
		 */
		scenarioRepetitions(1000, 1, 
				TickProducer.create(100l,0l,10000l),
				(ctxEvaluatorBuilder)-> ctxEvaluatorBuilder
					.with("internet-connection")
					.with("!battery-is-low")
					.with("doctor-is-present")
					.with("drug-is-available")
					.with("!patient-is-ok")
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
					.rem(1000l, "internet-connection")
					.add(2000l, "internet-connection")
					.rem(3000l, "!battery-is-low")
					.add(4000l, "!battery-is-low")
					.rem(5000l, "doctor-is-present")
					.add(6000l, "doctor-is-present")
					.rem(7000l, "drug-is-available")
					.add(8000l, "drug-is-available")
					.rem(9000l, "!patient-is-ok")
					.add(10000l, "!patient-is-ok")
				, evaluations, baseEvaluation);

		return evaluations;
	}
	
	
	
	protected IRepository getRepo(){
		return TASRepository.getRepo();
	}
}
