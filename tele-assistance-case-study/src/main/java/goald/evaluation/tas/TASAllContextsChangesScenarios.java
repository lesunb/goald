package goald.evaluation.tas;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.ExperimentExecutorAllContextsBased;
import goald.evaluation.timeline.TickProducer;
import goald.evaluation.timeline.TimelineEvaluation;
import goald.evaluation.timeline.TimelineTimer;
import goald.planning.VERespository;
import goald.repository.IRepository;


public class TASAllContextsChangesScenarios extends ExperimentExecutorAllContextsBased {
	
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
		scenarioRepetitions(100, 1, 
				TickProducer.create(100l,0l,12000l),
				(ctxList)-> {
					ctxList.add("internet-connection");
					ctxList.add("not-battery-is-low");
					ctxList.add("doctor-is-present");
					ctxList.add("xxx");
					ctxList.add("yyy");
				}
				,
				(qualityWeightsMap) -> {
					qualityWeightsMap.put("precision", 2);
					qualityWeightsMap.put("responseTime", 1);
					qualityWeightsMap.put("storegedSize", 1);
					qualityWeightsMap.put("usability", 1);
				},
				(goalsChangeBuilding)-> goalsChangeBuilding
					.addGoal("ProvideHealthSupport")
				, evaluations, baseEvaluation);

		return evaluations;
	}
	
	protected IRepository getRepo(){
		return TASRepository.getRepo();
	}
}
