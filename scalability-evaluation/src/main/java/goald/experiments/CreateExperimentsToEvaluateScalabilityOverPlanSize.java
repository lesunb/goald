package goald.experiments;

import goald.eval.spec.ExecSpec;
import goald.eval.spec.ExecSpecBuilder;
import goald.eval.spec.Experiment;
import goald.eval.spec.ExperimentBuilder;
import goald.evaluation.model.PlanningExperimentBuilder;


public class CreateExperimentsToEvaluateScalabilityOverPlanSize extends AbstractCreateExperiments {


	@Override
	protected Experiment createExperiments() {
		
		String expName = "scal_over_plan_size";
		
		final int EXP_REPETITION = 1000;
		
		ExperimentBuilder expBuilder = ((PlanningExperimentBuilder) PlanningExperimentBuilder
				.create()
				.setName(expName)
				.setResponseVariable("Time (ms)")
				.setConstant("numberOfChanges", 2)
				.setConstant("resultFileName", expName + ".dat")
				//experiment evaluated factors
				.addFactor("variability")
				.addFactor("numberOfGoals"))
				// params for creating the repository
				.putRepoSpec("depth", 2)
				.putRepoSpec("numOfDependencies", 5)
				.putRepoSpec("variabilityRange", 10, 10)
				.putRepoSpec("numberOfTrees", 1500)
				.putRepoSpec("contextSpace", getContextSpace(40))
				.putRepoSpec("contextVariabilityP",8)
				.putRepoSpec("contextVariabilityK",2);
		
		//model of the experiment. Set common variable accross all experiments
		ExecSpec model = ExecSpecBuilder.create()
				.put("type", 0)
				.put("variability", 10)
			.build();
		
		//create 10 series of equal experiments
		for(int i=0; i<EXP_REPETITION; i++){
			final Integer index = (Integer) i;
			//add new experiment specification, copying from model and setting values in ranges (goals and variability)
			addExecSpecsWithInRangeSetter(model, 
					1, 100, 10, // goals (from 1 to 100, step = 10 )
					(spec, value) ->{
				spec.put("numberOfGoals",value);
				spec.put("name", "num_goals_" + value);
				spec.put("index", index);
			}, expBuilder);
		}
			
		return expBuilder.build();
	}
	
}
