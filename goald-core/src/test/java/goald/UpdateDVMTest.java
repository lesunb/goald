package goald;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.model.CtxEvaluator;
import goald.model.Dependency;
import goald.model.GoalDManager;
import goald.model.VE;
import goald.model.util.AgentBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.DVMUpdater;
import goald.planning.VERespository;

public class UpdateDVMTest {
	
	DVMUpdater updater;
	VERespository repo;
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
		
		GoalDManager agent = AgentBuilder.create()
				.withContext(ctx)
				.build();
		
		updater = new DVMUpdater(repo, agent);
	}

	@Test
	public void testResolveVEInNoValidAlternative() {		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("alarm")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveVE(ve).getIsAchievable();
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
		boolean result = updater.resolveVE(ve).getIsAchievable();
		Assert.assertTrue(result);
		
		Assert.assertNotNull(ve.getChosenAlt());
		Assert.assertEquals(true, ve.getChosenAlt().getResolved());	
		
		VE ve2 = repo.queryRepo(query).get(0);		
		
		boolean result2 = updater.resolveVE(ve2).getIsAchievable();
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
		boolean result = updater.resolveVE(ve).getIsAchievable();
		Assert.assertTrue(result);
		
		// displayMyPosition
		
		Assert.assertNotNull(ve.getChosenAlt());
		Assert.assertEquals(true, ve.getChosenAlt().getResolved());	
		
		// check altenative children
		Assert.assertEquals(2, ve.getChosenAlt().getListDepVE().size());	
		//TODO check altenative grand children
	}
	
	@Test
	public void testResolveCondInNoApplicableContext() {		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("driveTips")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveVE(ve).getIsAchievable();
		Assert.assertFalse(true);
		Assert.assertNotNull(ve.getChosenAlt());	
	}
	
	@Test
	public void testResolveCondInApplicableContext() {		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("driveTips")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveVE(ve).getIsAchievable();
		Assert.assertFalse(true);
		Assert.assertNotNull(ve.getChosenAlt());	
	}
	
//	@Test
//	public void testResolveVEInNoValidAlternativeForAnyModifier() {		
//		List<Dependency> query = RepoQueryBuilder.create()
//				.queryFor("timeManager")
//				.build();
//		
//		VE ve = repo.queryRepo(query).get(0);		
//		
//		boolean result = updater.resolveVE(ve).getIsAchievable();
//		Assert.assertFalse(true);
//		Assert.assertNotNull(ve.getChosenAlt());	
//	}
}
