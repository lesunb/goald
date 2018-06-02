package goald.dam.planning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.dam.execution.DeploymentExecutor;
import goald.dam.model.Agent;
import goald.dam.model.ContextChange;
import goald.dam.model.CtxEvaluator;
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

		List<Goal> query = RepoQueryBuilder.create()
			.queryFor("displayMyPosition")
			.build();
		
		updater = new DamUpdater(repo, agent);
	
		Dame rootDame = updater.resolveGoals(query).get(0);		
				
		agent.setRootDame(rootDame);
	}
	
	@Test
	public void testCreateAInitialPlan() {
		DeploymentPlanner deploymentPlanner = new DeploymentPlanner(repo, agent);
		DeploymentPlan plan = deploymentPlanner.createPlan();
		
		assertNotNull(plan);
		assertEquals(6, plan.getCommands().size());
	}	
	
	@Test
	public void testCreateAPlanOfChange() {		
		DeploymentPlanner deploymentPlanner = new DeploymentPlanner(repo, agent);
		DeploymentPlan plan = deploymentPlanner.createPlan();

		DeploymentExecutor executor = new DeploymentExecutor(agent);
		executor.execute(plan);
		
		ContextChange change = ContextChangeBuilder.create()
				.remove("gps_capability")
				.build();
		
		ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
		
		handler.handle(change);
		
		DeploymentPlan plan2 = deploymentPlanner.createPlan();

		assertNotNull(plan);
		assertEquals(2, plan2.getCommands().size());
	}
	
	@Test
	public void testPlanForAnInvalidDeployment() {
		assertTrue(false);
	}
}
