package goald.evaluation.fillingstation;
import org.junit.Before;
import org.junit.Test;

import goald.AutonomousAgent;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.util.ContextChangeBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;
import goald.repository.IRepository;

public class FillingStationAdvisorTestSuite {

	AutonomousAgent agent;
	IRepository repo;
	
	@Before
	public void setup() {
		repo = FSARepository.getRepo();
	}
	
	@Test
	public void scenario1() {
		
		AutonomousAgent agent = new AutonomousAgent() {
			@Override
			public void setup(CtxEvaluatorBuilder initialCtx, 
					GoalsChangeRequestBuilder goals) {
						initialCtx.with(	"antenna_triangulation", 
								"protocol_get_fuel_level_and_mileage",
								"storage",
								"sound",
								"label");
						
						goals
						.addGoal("vehicle-refueling-is-assisted");
				
			}
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				System.out.println("scenario1");
				System.out.println(adaptPlan);
			}
		};
		
		agent.init(repo);
		
		agent.handleContextChange(
				ContextChangeBuilder.create()
				.add("gps_capability")
				.build());
		
		agent.handleContextChange(
				ContextChangeBuilder.create()
				.remove("antenna_triangulation")
				.build());
		
		agent.handleContextChange(
				ContextChangeBuilder.create()
				.remove("antenna_triangulation")
				.build());
	}
	
	@Test
	public void scenario2() {
		
		AutonomousAgent agent = new AutonomousAgent() {
			@Override
			public void setup(CtxEvaluatorBuilder initialCtx, 
					GoalsChangeRequestBuilder goals) {
						initialCtx.with("gps_capability",
								"protocol_on_board_computer_get_distante_to_empty",
								"internet_connection",
								"synthesized_voice");
						
						goals
						.addGoal("vehicle-refueling-is-assisted");
				
			}
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				System.out.println("scenario2");
				System.out.println(adaptPlan);
			}
		};
		
		agent.init(repo);
		
		agent.handleContextChange(
				ContextChangeBuilder.create()
				.add("gps_capability")
				.build());
		
		agent.handleContextChange(
				ContextChangeBuilder.create()
				.remove("antenna_triangulation")
				.build());
		
		agent.handleContextChange(
				ContextChangeBuilder.create()
				.remove("antenna_triangulation")
				.build());
	}
//	
//	@Test
//	public void scenario1() {
//		
//		DeploymentPlanningResult result = scenario("simple phone with ODB2", (agentBuilder)->{
//			agentBuilder.addContexts(
//				"antenna_triangulation", 
//				"protocol_get_fuel_level_and_mileage",
//				"storage",
//				"sound");
//		});
//		assertTrue(result.isSuccessfull());
//	}
//	
//	@Test
//	public void scenario2() {
//		
//		DeploymentPlanningResult result = scenario("smartphone with Bluethoth ODB2", (agentBuilder)->{
//			agentBuilder.addContexts(
//				"gps_capability",
//				"protocol_on_board_computer_get_distante_to_empty",
//				"internet_connection",
//				"synthesized_voice");
//		});
//		assertTrue(result.isSuccessfull());
//		
//	}
//	
//	@Test
//	public void scenario3() {
//		
//		DeploymentPlanningResult result = scenario("smartphone without car connection", (agentBuilder)->{
//			agentBuilder.addContexts(
//				"gps_capability", 
//				"internet_connection",
//				"synthesized_voice");
//		});
//		assertTrue(result.isSuccessfull());
//	
//	}
//	
//	@Test
//	public void scenario4() {
//		DeploymentPlanningResult result = scenario("dash computer with gps and no nav sys integration", (agentBuilder)->{
//			agentBuilder.addContexts(
//				"gps_capability", 
//				"protocol_on_board_computer_get_distante_to_empty",
//				"storage",
//				"visible_graphical_interface");
//		});
//		assertTrue(result.isSuccessfull());
//	}
//	
//	@Test
//	public void scenario5() {
//		
//		DeploymentPlanningResult result = scenario("dash computer, connected, with gps and nav sys integration", (agentBuilder)->{
//			agentBuilder.addContexts(
//				"gps_capability", 
//				"protocol_on_board_computer_get_distante_to_empty",
//				"internet_connection",
//				"interface_navigation_system");
//		});
//		assertTrue(result.isSuccessfull());
//	}
//	
//	@Test
//	public void scenario6() {
//		
//		DeploymentPlanningResult result = scenario("dash computer, connected, with gps and nav sys integration", (agentBuilder)->{
//			agentBuilder.addContexts(
//				"protocol_on_board_computer_get_distante_to_empty", 
//				"storage",
//				"synthesized_voice");
//		});
//		assertFalse(result.isSuccessfull());
//	}
//	
//	@Test
//	public void scenario7() {
//		
//		DeploymentPlanningResult result = scenario("dash computer, connected, with gps and nav sys integration", (agentBuilder)->{
//			agentBuilder.addContexts(
//				"gps_capability", 
//				"protocol_on_board_computer_get_distante_to_empty",
//				"interface_navigation_system");
//		});
//		assertFalse(result.isSuccessfull());
//	}

}
