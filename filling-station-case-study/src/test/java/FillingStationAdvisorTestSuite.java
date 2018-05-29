import org.junit.Before;
import org.junit.Test;

import goald.dam.model.DeploymentPlan;
import goald.dam.model.util.ContextChangeBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;
import goald.repository.IRepository;
import goalp.evaluation.fillingstation.FSARepository;

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

}
