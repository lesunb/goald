package goald;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.execution.DeploymentExecutor;
import goald.mock.FeelingStationAdvisorRepoMock;
import goald.model.ContextChange;
import goald.model.CtxEvaluator;
import goald.model.Dependency;
import goald.model.DeploymentPlan;
import goald.model.GoalDManager;
import goald.model.VE;
import goald.model.util.AgentBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.ContextChangeHandler;
import goald.planning.DVMUpdater;
import goald.planning.DeploymentPlanner;
import goald.planning.VERespository;

public class DeploymentPlanningTest {
	
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
				.build();
			
		agent = AgentBuilder.create()
			.withQualityWeight("precision", 3)
			.withQualityWeight("responseTime", 1)
			.withContext(ctx)
			.build();

		List<Dependency> query = RepoQueryBuilder.create()
			.queryFor("displayMyPosition")
			.build();
		
		updater = new DVMUpdater(repo, agent);
	
		VE rootDame = updater.resolveDepenencies(query).get(0);		
				
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
		
		ContextChange change3 = ContextChangeBuilder.create()
				.add("gps_capability")
				.build();
		handler.handle(change3);
		DeploymentPlan plan4 = deploymentPlanner.createPlan();
		assertEquals(2, plan4.getCommands().size());
	}
	
	@Test
	public void testPlanForAnInvalidDeployment() {
		
		DeploymentPlanner deploymentPlanner = new DeploymentPlanner(repo, agent);
		DeploymentPlan plan = deploymentPlanner.createPlan();

		DeploymentExecutor executor = new DeploymentExecutor(agent);
		executor.execute(plan);
		
		ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
		
		ContextChange change = ContextChangeBuilder.create()
				.remove("display_capability")
				.build();

		handler.handle(change);
		
		DeploymentPlan plan2 = deploymentPlanner.createPlan();
		assertEquals(6, plan2.getCommands().size());
		executor.execute(plan2);
		
		ContextChange change2 = ContextChangeBuilder.create()
				.add("display_capability")
				.build();
		
		handler.handle(change2);
		DeploymentPlan plan3 = deploymentPlanner.createPlan();
		assertEquals(6, plan3.getCommands().size());
	}
}
