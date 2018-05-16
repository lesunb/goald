

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.beliefs.model.Bundle;
import goald.beliefs.model.IRepository;
import goald.beliefs.util.BundleBuilder;
import goald.beliefs.util.RepositoryBuilder;
import goald.desires.model.Goal;

public class RepositoryTest {
	
	IRepository	repo;
	
	@Before
	public void setUp() throws Exception {
		
		repo = RepositoryBuilder.create()
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
					.requires("display_capability")
					.dependsOn("getPositionByGPS")
					.dependsOn("mapView")
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPositionByGPS.def")
					.defines("getPositionByGPS")
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPositionByGPS")
					.provides("getPositionByGPS")
					.requires("gps_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("mapView.def")
					.provides("mapView")
					.build())
				.add(
					BundleBuilder.create()
					.identification("mapView.impl")
					.provides("mapView")
					.requires("display_capability")
					.build())
				.build();
	}
	
	@Test
	public void testQueryForDefinition() {

		Goal goal = new Goal("greet");
		Bundle bundle = repo.queryForDefinition(goal);

		Assert.assertEquals("greater.def", bundle.identification);
		
	}
	
	@Test
	public void testQueryForImplementation() {

		Goal goal = new Goal("greet");
		List<Bundle> bundles = repo.queryForImplementations(goal);

		Assert.assertEquals(1, bundles.size());
		Assert.assertEquals("greater.impl", bundles.get(0).identification);
		
	}

}
