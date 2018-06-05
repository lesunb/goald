package goald;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.execution.DeploymentExecutor;
import goald.model.Agent;
import goald.model.ContextChange;
import goald.model.CtxEvaluator;
import goald.model.Dame;
import goald.model.DeploymentPlan;
import goald.model.Goal;
import goald.model.util.AgentBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.ContextChangeHandler;
import goald.planning.DamUpdater;
import goald.planning.DameRespository;
import goald.planning.DeploymentPlanner;

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

		assertNotNull(plan2);
		assertEquals(2, plan2.getCommands().size());
		executor.execute(plan2);
		
		ContextChange change2 = ContextChangeBuilder.create()
				.remove("gps_capability")
				.build();
		
		handler.handle(change2);
		
		DeploymentPlan plan3 = deploymentPlanner.createPlan();
		assertEquals(0, plan3.getCommands().size());
		
	}
	
	@Test
	public void testPlanForAnInvalidDeployment() {
		// assertTrue(false);
	}
}
