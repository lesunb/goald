package goald.dam.planning;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.model.Agent;
import goald.model.Dame;
import goald.model.Goal;
import goald.planning.DameRespository;

public class DameRepoTest {

	DameRespository repo;
	
	Agent agent;
	
	@Before
	public void setUp() throws Exception {	
		repo = FeelingStationAdvisorRepoMock.regRepo();
		
	}
	
	@Test
	public void testNotFoundDependency() {
		Goal goal = new Goal("nosense");
		List<Goal> goals = new ArrayList<>();
		goals.add(goal);

		List<Dame> result = repo.queryRepo(goals);
		Assert.assertNull(result);
	}
	
	@Test
	public void testWithOneDependency() {
		Goal goal = new Goal("greet");
		List<Goal> goals = new ArrayList<>();
		goals.add(goal);
		
		List<Dame> result = repo.queryRepo(goals);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertNotNull(result.get(0).getDefinition());
		Assert.assertNotNull(result.get(0).getAlts());
		Assert.assertEquals(1, result.get(0).getAlts().size());
	}

	@Test
	public void testWithTwoDependencies() {
		Goal goal = new Goal("getPosition");
		
		List<Goal> goals = new ArrayList<>();
		goals.add(goal);

		List<Dame> result = repo.queryRepo(goals);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertNotNull(result.get(0).getDefinition());
		Assert.assertNotNull(result.get(0).getAlts());
		Assert.assertEquals(2, result.get(0).getAlts().size());
	}
}
