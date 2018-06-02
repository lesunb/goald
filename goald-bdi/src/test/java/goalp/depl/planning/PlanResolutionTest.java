package goalp.depl.planning;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.beliefs.model.IRepository;
import goalp.model.Agent;
import goalp.model.AgentBuilder;
import goalp.model.ArtifactBuilder;
import goalp.model.DeploymentRequest;
import goalp.model.DeploymentRequestBuilder;
import goalp.systems.DeploymentPlanningResult;
import goalp.systems.IDeploymentPlanner;
import goalp.systems.InvalidDeploymentRequestException;
import goalp.systems.PlanSelectionException;
import goalp.systems.RepositoryBuilder;
import goalp.systems.SimpleDeploymentPlanner;

public class PlanResolutionTest {
	
	IDeploymentPlanner planner;
	
	IRepository repo;
	
	Agent agent;
	
	@Before
	public void setUp() {
		
		repo = RepositoryBuilder.create()
			.add(
				ArtifactBuilder.create()
				.identification("br.unb:greater:0.0.1")
				.provides("br.unb.greet")
				.condition("display_capability")
				.build())
			.add(
				ArtifactBuilder.create()
				.identification("br.unb:alarm:0.0.1")
				.provides("br.unb.alarm")
				.condition("sound_capability")
				.build())
			.add(
				ArtifactBuilder.create()
				.identification("br.unb:display_my_position:0.0.1")
				.provides("br.unb.display_my_position")
				.condition("display_capability")
				.dependsOn("br.unb:getPositionByGPS:0.0.1")
				.dependsOn("br.unb:mapView:0.0.1")
				.build())
			.add(
				ArtifactBuilder.create()
				.identification("br.unb:getPositionByGPS:0.0.1")
				.condition("gps_capability")
				.build())
			.add(
				ArtifactBuilder.create()
				.identification("br.unb:mapView:0.0.1")
				.condition("display_capability")
				.build())
			.build();
		
		planner = new SimpleDeploymentPlanner(repo);
		
		agent = AgentBuilder.create()
				.addContext("display_capability")
				.build();
	}
	
	@Test
	public void simplestArtifactSelectionTest() throws PlanSelectionException {
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoal("br.unb.greet")
				.build();
		

		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertTrue(result.isSuccessfull());
		Assert.assertEquals(1, result.getPlanSize());
		Assert.assertTrue(result.isPresentInThePlan("br.unb:greater:0.0.1"));
	}
	
	@Test
	public void artifactSelectionWithResourceRequirementTest() throws PlanSelectionException {
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoal("br.unb.alarm")
				.build();
		
		Agent agent = AgentBuilder.create()
				.addContext("display")
				.build();
		
		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertFalse(result.isSuccessfull());
	}
	
	@Test
	public void artifactSelectionWithDependencieTest() throws PlanSelectionException {
		DeploymentRequest request = DeploymentRequestBuilder.create()
			.addGoal("br.unb.display_my_position")
			.build();
		
		Agent agent = AgentBuilder.create()
			.addContext("display_capability")
			.addContext("gps_capability")
			.build();
		
		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertTrue(result.isSuccessfull());
		Assert.assertEquals(3, result.getPlanSize());
	}
	
	@Test
	public void artifactSelectionWithCyclicDependencieTest() throws PlanSelectionException {
	
		IRepository repoWithCycle = RepositoryBuilder.create()
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalA:0.0.1")
					.provides("br.unb.goalA")
					.dependsOn("br.unb.goalB")
					.build())
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalB:0.0.1")
					.provides("br.unb.goalB")
					.dependsOn("br.unb.goalA")
					.build())
				.build();
		
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoal("br.unb.goalA")
				.build();
			
		Agent agent = AgentBuilder.create()
			.build();
		
		IDeploymentPlanner planner = new SimpleDeploymentPlanner(repoWithCycle);
		
		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertTrue(result.isSuccessfull());
		Assert.assertEquals(2, result.getPlanSize());
	}
	
	@Test
	public void notProvidedRootGoalTest() throws PlanSelectionException {
		DeploymentRequest request = DeploymentRequestBuilder.create()
			.addGoal("br.unb.fly")
			.build();
		
		Agent agent = AgentBuilder.create()
			.addContext("display_capability")
			.addContext("gps_capability")
			.build();
		
		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertFalse(result.isSuccessfull());
	}
	
	@Test
	public void invalidContextRepoTest() throws PlanSelectionException {
		
		IRepository repo = RepositoryBuilder.create()
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:flyierWithBrum:0.0.1")
					.provides("br.unb.fly")
					.condition("flying-brum")
					.build())
				.build();
		
		DeploymentRequest request = DeploymentRequestBuilder.create()
			.addGoal("br.unb.fly")
			.build();
		
		
		Agent agent = AgentBuilder.create()
			.addContext("display_capability")
			.addContext("gps_capability")
			.build();
		
		IDeploymentPlanner planner = new SimpleDeploymentPlanner(repo);
		
		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertFalse(result.isSuccessfull());
	}
	@Test
	public void notProvidedDependencyGoalTest() throws PlanSelectionException {
		IRepository repoWithCycle = RepositoryBuilder.create()
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalA:0.0.1")
					.provides("br.unb.goalA")
					.dependsOn("br.unb.goalB")
					.build())
				.build();
		
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoal("br.unb.goalA")
				.build();
			
		Agent agent = AgentBuilder.create()
			.build();
		
		IDeploymentPlanner planner = new SimpleDeploymentPlanner(repoWithCycle);
		
		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertFalse(result.isSuccessfull());
	}
	
	@Test
	public void multiplesAlternativesTest() throws PlanSelectionException {
		IRepository repoWithCycle = RepositoryBuilder.create()
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalA:0.0.1")
					.provides("br.unb.goalA")
					.dependsOn("br.unb.goalB")
					.dependsOn("br.unb.goalX")
					.build())
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalB:0.0.1")
					.provides("br.unb.goalB")
					.dependsOn("br.unb.goalC")
					.build())
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalC:0.0.1")
					.provides("br.unb.goalC")
					.build())
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalB-nondependency:0.0.1")
					.provides("br.unb.goalB")
					.build())
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalX-nondependency:0.0.1")
					.provides("br.unb.goalX")
					.build())
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalX-dependency:0.0.1")
					.provides("br.unb.goalX")
					.dependsOn("br.unb.goalY")
					.build())
				.add(
					ArtifactBuilder.create()
					.identification("br.unb:goalY:0.0.1")
					.provides("br.unb.goalY")
					.build())
				.build();
		
		
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoal("br.unb.goalA")
				.build();
			
		Agent agent = AgentBuilder.create()
			.build();
		
		IDeploymentPlanner planner = new SimpleDeploymentPlanner(repoWithCycle);
		
		DeploymentPlanningResult result = planner.doPlan(request, agent);
		Assert.assertTrue(result.isSuccessfull());
	}
	
	@Test(expected = InvalidDeploymentRequestException.class)
	public void emptyRequestAndAgentTest() throws PlanSelectionException {
		planner.doPlan(null, null);
	}

	@Test(expected = InvalidDeploymentRequestException.class)
	public void emptyAgentTest() throws PlanSelectionException {
		Agent agent = AgentBuilder.create()
			.build();
		
		planner.doPlan(null, agent);
	}
	
	@Test(expected = InvalidDeploymentRequestException.class)
	public void emptyRequestTest() throws PlanSelectionException {
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoal("br.unb.goalA")
				.build();
		
		planner.doPlan(request, null);
	}

}
