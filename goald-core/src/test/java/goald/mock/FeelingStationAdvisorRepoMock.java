package goald.mock;

import goald.model.util.BundleBuilder;
import goald.planning.VERespository;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;

public class FeelingStationAdvisorRepoMock {

	public static VERespository regRepo() {

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
					.condition("display_capability")
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
					.condition("sound_capability")
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
					.condition("gps_capability")
					.withQuality("precision", 10)
					.withQuality("responseTime", 5)
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPositionByAntenna")
					.provides("getPosition")
					.condition("antenna_capability")
					.withQuality("precision", 5)
					.withQuality("responseTime", 10)
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
					.condition("display_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("timeManager.def")
					.defines("timeManager")
					.build())
				.add(
					BundleBuilder.create()
					.identification("timeManager.impl")
					.provides("timeManager")
					.dependsOnAny("alarm")
					.build())
				.add(
					BundleBuilder.create()
					.identification("premiumDriveTips.def")
					.defines("premiumDriveTips")
					.build())
				.add(
					BundleBuilder.create()
					.identification("premiumDriveTips.impl")
					.provides("premiumDriveTips")
					.dependsOnCond("nearby", "mapView")
					.build())
				.add(
					BundleBuilder.create()
					.identification("soundAlertWhileDriving.def")
					.defines("soundAlertWhileDriving")
					.build())
				.add(
					BundleBuilder.create()
					.identification("soundAlertWhileDriving.impl")
					.provides("soundAlertWhileDriving")
					.dependsOnCond("gps_capability", "displayMyPosition")
					.dependsOnCond("sound_alert_active", "alarm")
					.build())
					.build();
		
		return new VERespository(_repo);
	}
}
