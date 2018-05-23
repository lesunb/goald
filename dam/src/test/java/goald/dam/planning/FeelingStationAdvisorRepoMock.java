package goald.dam.planning;

import goald.dam.model.util.BundleBuilder;
import goalp.repository.IRepository;
import goalp.repository.RepositoryBuilder;

public class FeelingStationAdvisorRepoMock {

	public static DameRespository regRepo() {

		IRepository _repo = RepositoryBuilder.create()
				.add(
					BundleBuilder.create()
					.identification("greater.def")
					.defines("greet")
					.build())
				.add(
					BundleBuilder.create()
					.identification("greater.impl")
					.provides("greet")
					.requires("display_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("alarm.def")
					.defines("alarm")
					.build())
				.add(
					BundleBuilder.create()
					.identification("alarm")
					.provides("alarm")
					.requires("sound_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("displayMyPosition.def")
					.defines("displayMyPosition")
					.build())
				.add(
					BundleBuilder.create()
					.identification("displayMyPosition.impl")
					.provides("displayMyPosition")
					.dependsOn("getPosition")
					.dependsOn("mapView")
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPosition.def")
					.defines("getPosition")
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPositionByGPS")
					.provides("getPosition")
					.requires("gps_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPositionByAntenna")
					.provides("getPosition")
					.requires("antenna_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("mapView.def")
					.defines("mapView")
					.build())
				.add(
					BundleBuilder.create()
					.identification("mapView.impl")
					.provides("mapView")
					.requires("display_capability")
					.build())
				.build();
		return new DameRespository(_repo);
	}
}
