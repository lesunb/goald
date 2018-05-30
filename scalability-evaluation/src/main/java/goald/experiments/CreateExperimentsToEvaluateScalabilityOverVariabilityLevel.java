package goald.experiments;

import goald.eval.spec.ExecSpec;
import goald.eval.spec.ExecSpecBuilder;
import goald.eval.spec.Experiment;
import goald.eval.spec.ExperimentBuilder;
import goalp.evaluation.model.PlanningExperimentBuilder;

public class CreateExperimentsToEvaluateScalabilityOverVariabilityLevel extends AbstractCreateExperiments {

	@Override
	protected Experiment createExperiments() {
		
		String expName = "Plan Size vs Variability vs Planning Time (ms) - 10 execs";
		
		ExperimentBuilder expBuilder = ((PlanningExperimentBuilder)PlanningExperimentBuilder
				.create()
				.setName(expName)
				.setResponseVariable("Time (ms)")
				//experiment evaluated factors
				.addFactor("Variability")
				.addFactor("Plan size"))
				// params for creating the repository
				.putRepoSpec("depth", 2)
				.putRepoSpec("numOfDependencies", 5)
				.putRepoSpec("variabilityRange", 1, 10)
				.putRepoSpec("numberOfTrees", 100)
				.putRepoSpec("contextSpace", getContextSpace(20))
				.putRepoSpec("agentContext", getContextSpace(20))
				.putRepoSpec("contextVariabilityP",5)
				.putRepoSpec("contextVariabilityK",2);
		
		//model of the experiment. Set common variable accross all experiments
		ExecSpec model = ExecSpecBuilder.create()
				.put("type", 0)
				.put("agentContext", getContextSpace(20))
			.build();
		
		//create 10 repetitions of equal experiments
		for(int i=0; i<10; i++){
			
			//create execution specification from a range of k combination, from 0
			addExecSpecsWithTwoInRangeSetter(model, 
					0, 100, 1, //goals (start, end, step)
					1, 10, 1, //variability  (start, end, step)
					(spec, rangeValues) ->{
				spec.put("numberOfGoals", rangeValues[0]);
				spec.put("variability", rangeValues[1]);
			}, expBuilder);
		}
			
		return expBuilder.build();
	}
}
