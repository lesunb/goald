package goald.dam.planning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Agent;
import goald.dam.model.CtxEvaluator;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.GoalsChangeRequest;
import goald.dam.model.util.AgentBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;

public class HandleGoalsChangeTest {
	
	DamUpdater updater;
	DameRespository repo;
	Agent agent;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("antenna_capability")
				.with("display_capability")
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
}
