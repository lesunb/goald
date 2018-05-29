package goalp.evaluation.fillingstation;

import goald.dam.model.util.BundleBuilder;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;

public class FSARepository {

	public static IRepository getRepo() {
		return RepositoryBuilder.create()
		.add(
			BundleBuilder.create()
			.identification("vehicle-refueling-is-assisted-definition")
			.defines("vehicle-refueling-is-assisted")
			.build())
		.add(
			BundleBuilder.create()
			.identification("vehicle-refueling-is-assisted-strategy")
			.provides("vehicle-refueling-is-assisted")
			.dependsOn("get_position")
			.dependsOn("get_distance_to_empty")
			.dependsOn("access_filling_station_information")
			.dependsOn("decide_convenient_station")
			.dependsOn("driver_is_notified")
			.build())
		/* 2nd level definitions */
		.add(
			BundleBuilder.create()
			.identification("get_position-definition")
			.defines("get_position")
			.build())
		.add(
			BundleBuilder.create()
			.identification("get_distance_to_empty-definition")
			.defines("get_distance_to_empty")
			.build())
		.add(
			BundleBuilder.create()
			.identification("access_filling_station_information-definition")
			.defines("access_filling_station_information")
			.build())
		.add(
			BundleBuilder.create()
			.identification("decide_convenient_station-definition")
			.defines("decide_convenient_station")
			.build())
		.add(
			BundleBuilder.create()
			.identification("driver_is_notified-definition")
			.defines("driver_is_notified")
			.build())
		.add(
			BundleBuilder.create()
			.identification("get_distance_to_empty-definition")
			.defines("get_distance_to_empty")
			.build())
		/* positioning plans */
		.add(
			BundleBuilder.create()
			.identification("get_position_gps")
			.provides("get_position")
			.requires("gps_capability")
			.build())
		.add(
			BundleBuilder.create()
			.identification("get_position_antenna_triangulation")
			.provides("get_position")
			.requires("antenna_triangulation")
			.build())
		.add(
			BundleBuilder.create()
			.identification("get_position_using_nearby_wifi")
			.provides("get_position")
			.requires("wifi")
			.build())
		/* get distance alternatives */
		.add(
			BundleBuilder.create()
			.identification("get_distance_to_empty-from-on-board-computer")
			.provides("get_distance_to_empty")
			.requires("protocol_on_board_computer_get_distante_to_empty")
			.build())
		.add(
			BundleBuilder.create()
			.identification("calculate_distance_to_empty_based_on_fuel_level_and_mileage")
			.provides("get_distance_to_empty")
			.requires("protocol_get_fuel_level_and_mileage")
			.build())
		.add(/* further divide */
			BundleBuilder.create()
			.identification("get_distance_to_empty_based_on_user_input_and_distance_track")
			.provides("get_distance_to_empty")
			.requires("gps_capability")
			.build())
		/* access_filling_station_information alternatives */
		.add(
			BundleBuilder.create()
			.identification("access_filling_station_information_online_impl")
			.provides("access_filling_station_information")
			.requires("internet_connection")
			.build())
		.add(
			BundleBuilder.create()
			.identification("access_filling_station_information_offline_impl")
			.provides("access_filling_station_information")
			.requires("storage")
			.build())
		/* decide convenient station alternatives */
		.add(
			BundleBuilder.create()
			.identification("decide_convenient_station_impl")
			.provides("decide_convenient_station")
			.build())
		/* driver is notified alternatives */
		.add(
			BundleBuilder.create()
			.identification("driver_is_notified_by_navigation_system")
			.provides("driver_is_notified")
			.requires("interface_navigation_system")
			.build())
		.add(
			BundleBuilder.create()
			.identification("alert_driver_by_sound_using_synthesized_voice")
			.provides("driver_is_notified")
			.requires("synthesized_voice")
			.build())
		.add(
			BundleBuilder.create()
			.identification("alert_driver_by_sound_using_pre_recorded_voice")
			.provides("driver_is_notified")
			.requires("sound")
			.build())
		.add(
			BundleBuilder.create()
			.identification("alert_driver_by_visual_sign")
			.provides("driver_is_notified")
			.requires("visible_graphical_interface")
			.build())
		.build();
	}
}
