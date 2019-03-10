package goald;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.model.Bundle;
import goald.model.util.BundleBuilder;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;

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
					.condition("display_capability")
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
					.condition("display_capability")
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
					.condition("gps_capability")
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
					.condition("display_capability")
					.build())
				.build();
	}
	
	@Test
	public void testQueryForDefinition() {
		Bundle bundle = repo.queryForDefinition("greet");

		Assert.assertEquals("greater.def", bundle.identification);
		
	}
	
	@Test
	public void testQueryForImplementation() {
		List<Bundle> bundles = repo.queryForImplementations("greet");

		Assert.assertEquals(1, bundles.size());
		Assert.assertEquals("greater.impl", bundles.get(0).identification);
		
	}

}
