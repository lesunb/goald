package goald;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.analysis.DVMUpdater;
import goald.mock.FeelingStationAdvisorRepoMock;
import goald.model.CtxEvaluator;
import goald.model.Dependency;
import goald.model.GoalDManager;
import goald.model.VE;
import goald.model.util.AgentBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.VERespository;

public class UpdateDVMTest {
	
	DVMUpdater updater;
	VERespository repo;
	CtxEvaluator gpsctx;
	GoalDManager mangerWithGPS;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		
		 gpsctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
		
		 mangerWithGPS = AgentBuilder.create()
				.withContext(gpsctx)
				.build();
	}

	@Test
	public void testResolveVEInNoValidAlternative() {		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("alarm")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		DVMUpdater updater = new DVMUpdater(repo, mangerWithGPS);
		
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertFalse(result);
		Assert.assertNull(ve.getChosenAlt());	
	}
	
	@Test
	public void testResolveVEOneValidAlternative() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
	
		GoalDManager agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		DVMUpdater updater = new DVMUpdater(repo, agent);
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertTrue(result);
		
		Assert.assertNotNull(ve.getChosenAlt());
		Assert.assertEquals(true, ve.getChosenAlt().getResolved());	
		
		VE ve2 = repo.queryRepo(query).get(0);		
		
		boolean result2 = updater.resolveVE(ve2).isAchievable();
		Assert.assertTrue(result2);
		
		Assert.assertNotNull(ve2.getChosenAlt());
		Assert.assertEquals(true, ve2.getChosenAlt().getResolved());	
	}
	
	@Test
	public void testResolveVEMultipleAlternativeDependencies() {

		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability", "display_capability")
				.build();
		
		GoalDManager manager = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("displayMyPosition")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		DVMUpdater updater = new DVMUpdater(repo, manager);
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertTrue(result);
		
		// displayMyPosition
		
		Assert.assertNotNull(ve.getChosenAlt());
		Assert.assertEquals(true, ve.getChosenAlt().getResolved());	
		
		// check altenative children
		Assert.assertEquals(2, ve.getChosenAlt().getListDepVE().size());	
		//TODO check altenative grand children
	}
	
	@Test
	public void testResolveVEAnyDependencyWithNoneValidAlternative() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				//.with("gps_capability", "display_capability")
				.build();
		
		GoalDManager manager = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("displayORAlert")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		DVMUpdater updater = new DVMUpdater(repo, manager);
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertFalse(result);
		// check altenative children
		
	}
	
	@Test
	public void testResolveVEAnyDependencyWithOneValidAlternativeTest1() {		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("display_capability")
				.build();
		
		GoalDManager manager = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("displayORAlert")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);
		
		DVMUpdater updater = new DVMUpdater(repo, manager);
		// displayMyPosition
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertTrue(result);

		Assert.assertNotNull(ve.getChosenAlt());
		Assert.assertEquals(true, ve.getChosenAlt().getResolved());
	}
	
	@Test
	public void testResolveVEAnyDependencyWithOneValidAlternativeTest2() {		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("sound_capability")
				.build();
		
		GoalDManager manager = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("displayORAlert")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);
		
		DVMUpdater updater = new DVMUpdater(repo, manager);
		
		// displayMyPosition
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertTrue(result);

		Assert.assertNotNull(ve.getChosenAlt());
		Assert.assertEquals(true, ve.getChosenAlt().getResolved());
	}
}
