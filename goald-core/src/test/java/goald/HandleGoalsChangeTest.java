package goald;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import goald.model.GoalDManager;
import goald.analysis.DVMUpdater;
import goald.mock.FeelingStationAdvisorRepoMock;
import goald.model.CtxEvaluator;
import goald.model.GoalsChangeRequest;
import goald.model.util.AgentBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.VERespository;
import goald.planning.GoalsChangeHandler;

public class HandleGoalsChangeTest {
	
	DVMUpdater updater;
	VERespository repo;
	GoalDManager agent;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("antenna_capability")
				.with("display_capability")
				.with("sound_capability")
				.build();
			
			agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();

	}
	
	@Test
	public void testCreateAInitialPlan() {
		
		GoalsChangeRequest change = GoalsChangeRequestBuilder.create()
		.addGoal("displayMyPosition")
		.build();
		
		GoalsChangeHandler handler = new GoalsChangeHandler(repo, agent);
		handler.handle(change);
				
		assertNotNull(agent.getRootDame());
	}
	
	@Test
	public void testHandleTowGoals() {
		
		GoalsChangeRequest change = GoalsChangeRequestBuilder.create()
		.addGoal("displayMyPosition")
		.addGoal("alarm")
		.build();
		
		GoalsChangeHandler handler = new GoalsChangeHandler(repo, agent);
		handler.handle(change);
				
		assertNotNull(agent.getRootDame());
		assertEquals("displayMyPosition.impl", agent.getRootDame().getChosenAlt().getListDepVE().get(0).getChosenAlt().getImpl().identification);
		assertEquals("alarm", agent.getRootDame().getChosenAlt().getListDepVE().get(1).getChosenAlt().getImpl().identification);
	}

}
