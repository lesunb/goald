package goalp.evaluation.fillingstation;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.beliefs.model.IRepository;
import goalp.exputil.AbstractStudyCase;
import goalp.exputil.ExperimentTimer;
import goalp.exputil.WriteService;
import goalp.model.ArtifactBuilder;
import goalp.systems.IDeploymentPlanner;
import goalp.systems.RepositoryBuilder;

public class FillingStationStudyCase extends AbstractStudyCase {

	IDeploymentPlanner planner;
	
	IRepository repo;
	
	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;
	
	@Inject
	WriteService write;
	

	public void caseStudy() {
		for(int i =0; i<100; i++){
			doCaseStudy();
		}
	}
	
	public void doCaseStudy() {
		/* 
		 * Study case scenarios. Each one define a different set of contexts. 
		 * For each one the deployment will be planned
		 */
		scenario("s1", (agentBuilder)->{
			agentBuilder.addContexts(
				"antenna_triangulation", 
				"protocol_get_fuel_level_and_mileage",
				"storage",
				"sound");
		});
		
		scenario("s2", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability",
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"synthesized_voice");
		});
		
		scenario("s3", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"internet_connection",
				"synthesized_voice");
		});
	
		scenario("s4", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"storage",
				"visible_graphical_interface");
		});
		
		scenario("s5", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"internet_connection",
				"interface_navigation_system");
		});
		
		
		scenario("s6", (agentBuilder)->{
			agentBuilder.addContexts(
				"protocol_on_board_computer_get_distante_to_empty", 
				"storage",
				"synthesized_voice");
		});
		
		scenario("s7", (agentBuilder)->{
			agentBuilder.addContexts(
				"gps_capability", 
				"protocol_on_board_computer_get_distante_to_empty",
				"interface_navigation_system");
		});

	}
	
	protected void setupEnvironment(RepositoryBuilder repositoryBuilder){
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

}
