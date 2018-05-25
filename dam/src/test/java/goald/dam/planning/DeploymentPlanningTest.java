package goald.dam.planning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Agent;
import goald.dam.model.ContextChange;
import goald.dam.model.Dame;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.Goal;
import goald.dam.model.util.AgentBuilder;
import goald.dam.model.util.ContextChangeBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.RepoQueryBuilder;

public class DeploymentPlanningTest {
	
	DamUpdater updater;
	DameRespository repo;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
	}
	
	@Test
	public void testCreateAInitialPlan() {
				
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
			.with("gps_capability")
			.with("display_capability")
			.build();
		
		Agent agent = AgentBuilder.create()
			.withQualityWeight("precision", 3)
			.withQualityWeight("responseTime", 1)
			.withContext(ctx)
			.build();

		List<Goal> query = RepoQueryBuilder.create()
			.queryFor("displayMyPosition")
			.build();
		
		updater = new DamUpdater(repo, agent);
	
		Dame rootDame = updater.resolveGoals(query).get(0);		
				
		agent.setRootDame(rootDame);
				
		DeploymentPlanner deploymentPlanner = new DeploymentPlanner(repo, agent);
		DeploymentPlan plan = deploymentPlanner.createPlan(rootDame);
		
		assertNotNull(plan);
		assertEquals(6, plan.getCommands().size());
				
	}
}
