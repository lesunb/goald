package goalp.evaluation.plans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.eval.exec.ExperimentSetup;
import goald.evaluation.exec.PrismRepositoryFactory;
import goald.evaluation.model.PlanningExperiment;
import goald.evaluation.model.PlanningExperimentBuilder;
import goald.model.Bundle;
import goald.model.Goal;

public class PrismRespositoryBuilderTest {

	ExperimentSetup setup;
	
	@Before
	public void setup() {
	
		PlanningExperimentBuilder expBuilder = ((PlanningExperimentBuilder)PlanningExperimentBuilder
				.create()
				.setName("teste repo")
				.setResponseVariable("Time (ms)")
				//experiment evaluated factors
				.addFactor("Variability")
				.addFactor("Plan size"))
				// params for creating the repository
				.putRepoSpec("depth", 4)
				.putRepoSpec("numOfDependencies", 5)
				.putRepoSpec("variabilityRange", 4, 4)
				.putRepoSpec("numberOfTrees", 3)
				.putRepoSpec("contextSpace", getContextSpace(20))
				.putRepoSpec("agentContext", getContextSpace(20))
				.putRepoSpec("contextVariabilityP",5)
				.putRepoSpec("contextVariabilityK",2);
		
		PlanningExperiment experiment = expBuilder.build();
		
		setup = new ExperimentSetup();
		
		PrismRepositoryFactory
		.create()
		.buildBySpec(experiment.getRepoSpec())
		.setSetupWithRepo(setup)
		.setSetupRootGoals(setup);
	}
	
	protected List<String> getContextSpace(int size) {
		String[] space = new String[size];
		for(int i = 0; i< size; i++){
			space[i] = "C"+i;
		}
		return Arrays.asList(space);
	}

	@Test
	public void testCreatedRepository() {
		assertEquals(10311, setup.getRepository().getSize());
		
		Goal nextGoal = new Goal(setup.getRootGoals().get(0));
		Bundle def, impl;
		int level = 0;
		do {
			level++;
			def = setup.getRepository().queryForDefinition(nextGoal);
			assertNotNull(def);
			impl = setup.getRepository().queryForImplementations(nextGoal).get(0);
			assertNotNull(impl);
			if(impl.getDepends().size() > 0) {
				nextGoal = impl.getDepends().get(0);	
			}else {
				nextGoal = null;
			}
		}while(nextGoal != null);
		
		assertEquals(2, impl.getConditions().size());
		assertEquals(5, level);
		assertEquals("num of alternatives", 4, setup.getRepository().queryForImplementations(def.getDefines().get(0)).size());
	}
}
