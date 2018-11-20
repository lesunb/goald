package goald;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.model.Dependency;
import goald.model.Goal;
import goald.model.GoalDManager;
import goald.model.VE;
import goald.planning.VERespository;

public class VERepoTest {

	VERespository repo;
	
	GoalDManager agent;
	
	@Before
	public void setUp() throws Exception {	
		repo = FeelingStationAdvisorRepoMock.regRepo();
		
	}
	
	@Test
	public void testNotFoundDependency() {
		Dependency goal = new Dependency("nosense");
		List<Dependency> goals = new ArrayList<>();
		goals.add(goal);

		List<VE> result = repo.queryRepo(goals);
		Assert.assertNull(result);
	}
	
	@Test
	public void testWithOneDependency() {
		Dependency goal = new Dependency("greet");
		List<Dependency> goals = new ArrayList<>();
		goals.add(goal);
		
		List<VE> result = repo.queryRepo(goals);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertNotNull(result.get(0).getDefinition());
		Assert.assertNotNull(result.get(0).getAlts());
		Assert.assertEquals(1, result.get(0).getAlts().size());
	}

	@Test
	public void testWithTwoDependencies() {
		Dependency goal = new Dependency("getPosition");
		
		List<Dependency> goals = new ArrayList<>();
		goals.add(goal);

		List<VE> result = repo.queryRepo(goals);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertNotNull(result.get(0).getDefinition());
		Assert.assertNotNull(result.get(0).getAlts());
		Assert.assertEquals(2, result.get(0).getAlts().size());
	}
}
