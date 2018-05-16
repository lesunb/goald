package goald.dam.planning;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Agent;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.util.BundleBuilder;
import goalp.repository.IRepository;
import goalp.repository.RepositoryBuilder;

public class QueryRepoTest {

	IRepository repo;
	
	Agent agent;
	
	@Before
	public void setUp() throws Exception {
		
		
		Agent agent = new Agent();
		
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
	public void testNotFoundDependency() {
		Goal goal = new Goal("nosense");
		List<Goal> goals = new ArrayList<>();
		goals.add(goal);
		
		UpdateDame updater  = new UpdateDame(repo, agent);
	
		List<Dame> result = updater.queryRepo(goals);
		Assert.assertNull(result);
	}
	
	@Test
	public void testWithOneDependency() {
		Goal goal = new Goal("greet");
		List<Goal> goals = new ArrayList<>();
		goals.add(goal);
		
		UpdateDame updater  = new UpdateDame(repo, agent);
	
		List<Dame> result = updater.queryRepo(goals);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertNotNull(result.get(0).getDefinition());
		Assert.assertNotNull(result.get(0).getAlts());
		Assert.assertEquals(1, result.get(0).getAlts().size());
	}

	@Test
	public void testWithTwoDependency() {
		Goal goal = new Goal("getPosition");
		
		List<Goal> goals = new ArrayList<>();
		goals.add(goal);
		
		UpdateDame updater  = new UpdateDame(repo, agent);
	
		List<Dame> result = updater.queryRepo(goals);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertNotNull(result.get(0).getDefinition());
		Assert.assertNotNull(result.get(0).getAlts());
		Assert.assertEquals(2, result.get(0).getAlts().size());
	}
}
