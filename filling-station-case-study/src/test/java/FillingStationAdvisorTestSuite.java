import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.weld.log.LoggerProducer;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import goalp.evaluation.ExperimentTimerImpl;
import goalp.exputil.AbstractPlanningTestCase;
import goalp.model.ArtifactBuilder;
import goalp.systems.DeploymentPlanningResult;
import goalp.systems.RepositoryBuilder;
import goalp.systems.SimpleDeploymentPlanner;

@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class, SimpleDeploymentPlanner.class, ExperimentTimerImpl.class})
public class FillingStationAdvisorTestSuite extends AbstractPlanningTestCase {

	
	@Before
	public void setup() {
		super.setup();
	}
	
	protected void insantiateRepository(RepositoryBuilder repositoryBuilder){
		repositoryBuilder
		.add(
			ArtifactBuilder.create()
			.identification("vehicle-refueling-is-assisted-definition:0.0.1")
			.provides("vehicle-refueling-is-assisted-definition:0.0.1")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("vehicle-refueling-is-assisted-strategy:0.0.1")
			.provides("vehicle-refueling-is-assisted:0.0.1")
			.dependsOn("vehicle-refueling-is-assisted-definition:0.0.1")
			.dependsOn("get_position-definition:0.0.1")
			.dependsOn("get_distance_to_empty-definition:0.0.1")
			.dependsOn("decide_convenient_station-definition:0.0.1")
			.dependsOn("driver_is_notified-definition:0.0.1")
			.dependsOn("get_position:0.0.1")
			.dependsOn("get_distance_to_empty:0.0.1")
			.dependsOn("access_filling_station_information:0.0.1")
			.dependsOn("decide_convenient_station:0.0.1")
			.dependsOn("driver_is_notified:0.0.1")
			.build())
		/* 2nd level definitions */
		.add(
			ArtifactBuilder.create()
			.identification("get_position-definition:0.0.1")
			.provides("get_position-definition:0.0.1")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("get_distance_to_empty-definition:0.0.1")
			.provides("get_distance_to_empty-definition:0.0.1")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("access_filling_station_information-definition:0.0.1")
			.provides("access_filling_station_information-definition:0.0.1")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("decide_convenient_station-definition:0.0.1")
			.provides("decide_convenient_station-definition:0.0.1")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("driver_is_notified-definition:0.0.1")
			.provides("driver_is_notified-definition:0.0.1")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("get_distance_to_empty-definition:0.0.1")
			.provides("get_distance_to_empty-definition:0.0.1")
			.build())
		/* positioning plans */
		.add(
			ArtifactBuilder.create()
			.identification("get_position_gps:0.0.1")
			.provides("get_position:0.0.1")
			.condition("gps_capability")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("get_position_antenna_triangulation:0.0.1")
			.provides("get_position:0.0.1")
			.condition("antenna_triangulation")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("get_position_using_nearby_wifi:0.0.1")
			.provides("get_position:0.0.1")
			.condition("wifi")
			.build())
		/* get distance alternatives */
		.add(
			ArtifactBuilder.create()
			.identification("get_distance_to_empty-from-on-board-computer:0.0.1")
			.provides("get_distance_to_empty:0.0.1")
			.condition("protocol_on_board_computer_get_distante_to_empty")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("calculate_distance_to_empty_based_on_fuel_level_and_mileage:0.0.1")
			.provides("get_distance_to_empty:0.0.1")
			.condition("protocol_get_fuel_level_and_mileage")
			.build())
		.add(/* further divide */
			ArtifactBuilder.create()
			.identification("get_distance_to_empty_based_on_user_input_and_distance_track:0.0.1")
			.provides("get_distance_to_empty:0.0.1")
			.condition("gps_capability")
			.build())
		/* access_filling_station_information alternatives */
		.add(
			ArtifactBuilder.create()
			.identification("access_filling_station_information_online_impl:0.0.1")
			.provides("access_filling_station_information:0.0.1")
			.dependsOn("access_filling_station_information-definition:0.0.1")
			.condition("internet_connection")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("access_filling_station_information_offline_impl:0.0.1")
			.provides("access_filling_station_information:0.0.1")
			.dependsOn("access_filling_station_information-definition:0.0.1")
			.condition("storage")
			.build())
		/* decide convenient station alternatives */
		.add(
			ArtifactBuilder.create()
			.identification("decide_convenient_station_impl:0.0.1")
			.provides("decide_convenient_station:0.0.1")
			.dependsOn("decide_convenient_station-definition:0.0.1")
			.build())
		/* driver is notified alternatives */
		.add(
			ArtifactBuilder.create()
			.identification("driver_is_notified_by_navigation_system:0.0.1")
			.provides("driver_is_notified:0.0.1")
			.dependsOn("driver_is_notified-definition:0.0.1")
			.condition("interface_navigation_system")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("alert_driver_by_sound_using_synthesized_voice:0.0.1")
			.provides("driver_is_notified:0.0.1")
			.dependsOn("driver_is_notified-definition:0.0.1")
			.condition("synthesized_voice")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("alert_driver_by_sound_using_pre_recorded_voice:0.0.1")
			.provides("driver_is_notified:0.0.1")
			.dependsOn("driver_is_notified-definition:0.0.1")
			.condition("sound")
			.build())
		.add(
			ArtifactBuilder.create()
			.identification("alert_driver_by_visual_sign:0.0.1")
			.provides("driver_is_notified:0.0.1")
			.dependsOn("driver_is_notified-definition:0.0.1")
			.condition("visible_graphical_interface")
			.build());
	}
	
	@Test
	public void scenario1() {
		
		DeploymentPlanningResult result = scenario("simple phone with ODB2", (agentBuilder)->{
			agentBuilder.addContexts(
				"antenna_triangulation", 
				"protocol_get_fuel_level_and_mileage",
				"storage",
				"sound");
		});
		assertTrue(result.isSuccessfull());
	}
	
	@Test
	public void scenario2() {
		
		DeploymentPlanningResult result = scenario("smartphone with Bluethoth ODB2", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability",
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"synthesized_voice");
		});
		assertTrue(result.isSuccessfull());
		
	}
	
	@Test
	public void scenario3() {
		
		DeploymentPlanningResult result = scenario("smartphone without car connection", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"internet_connection",
				"synthesized_voice");
		});
		assertTrue(result.isSuccessfull());
	
	}
	
	@Test
	public void scenario4() {
		DeploymentPlanningResult result = scenario("dash computer with gps and no nav sys integration", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"storage",
				"visible_graphical_interface");
		});
		assertTrue(result.isSuccessfull());
	}
	
	@Test
	public void scenario5() {
		
		DeploymentPlanningResult result = scenario("dash computer, connected, with gps and nav sys integration", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"interface_navigation_system");
		});
		assertTrue(result.isSuccessfull());
	}
	
	@Test
	public void scenario6() {
		
		DeploymentPlanningResult result = scenario("dash computer, connected, with gps and nav sys integration", (agentBuilder)->{
			agentBuilder.addContexts(
				"protocol_on_board_computer_get_distante_to_empty", 
				"storage",
				"synthesized_voice");
		});
		assertFalse(result.isSuccessfull());
	}
	
	@Test
	public void scenario7() {
		
		DeploymentPlanningResult result = scenario("dash computer, connected, with gps and nav sys integration", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"interface_navigation_system");
		});
		assertFalse(result.isSuccessfull());
	}
}
