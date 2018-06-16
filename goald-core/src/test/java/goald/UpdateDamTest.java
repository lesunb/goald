package goald;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.model.Agent;
import goald.model.Alternative;
import goald.model.CtxEvaluator;
import goald.model.Dame;
import goald.model.Goal;
import goald.model.util.AgentBuilder;
import goald.model.util.AlternativeBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.DamUpdater;
import goald.planning.DameRespository;

public class UpdateDamTest {
	
	DamUpdater updater;
	DameRespository repo;
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
		
		Agent agent = AgentBuilder.create()
				.withContext(ctx)
				.build();
		
		updater = new DamUpdater(repo, agent);
	}
	

	@Test
	public void testResolveIfLeaf() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
		.with("C1")
		.build();
		
		Alternative alt = new Alternative();
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertTrue(result);
	}

	
	@Test
	public void testResolveAltInANotValidContext() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("C1")
				.build();
		
		Alternative alt = AlternativeBuilder
				.create()
				.requiresCtx("C1", "C2")
				.build();
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertFalse(result);
	}
	
	@Test
	public void testResolveAltInAContextValid() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("C1", "C2", "C3")
				.build();
		
		Alternative alt = AlternativeBuilder
				.create()
				.requiresCtx("C1", "C2")
				.build();
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void testResolveDameInNoValidAlternative() {		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("alarm")
				.build();
		
		Dame dame = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveDame(dame);
		Assert.assertFalse(result);
		Assert.assertNull(dame.getChosenAlt());	
	}
	
	@Test
	public void testResolveDameOneValidAlternative() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
	
		Agent agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		Dame dame = repo.queryRepo(query).get(0);		
		
		DamUpdater updater = new DamUpdater(repo, agent);
		boolean result = updater.resolveDame(dame);
		Assert.assertTrue(result);
		
		Assert.assertNotNull(dame.getChosenAlt());
		Assert.assertEquals(true, dame.getChosenAlt().getResolved());	
		
		Dame dame2 = repo.queryRepo(query).get(0);		
		
		boolean result2 = updater.resolveDame(dame2);
		Assert.assertTrue(result2);
		
		Assert.assertNotNull(dame2.getChosenAlt());
		Assert.assertEquals(true, dame2.getChosenAlt().getResolved());	
	}
	
	@Test
	public void testResolveDameMultipleAlternativeDependencies() {

		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability", "display_capability")
				.build();
		
		Agent agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("displayMyPosition")
				.build();
		
		Dame dame = repo.queryRepo(query).get(0);		
		
		DamUpdater updater = new DamUpdater(repo, agent);
		boolean result = updater.resolveDame(dame);
		Assert.assertTrue(result);
		
		// displayMyPosition
		
		Assert.assertNotNull(dame.getChosenAlt());
		Assert.assertEquals(true, dame.getChosenAlt().getResolved());	
		
		// check altenative children
		Assert.assertEquals(2, dame.getChosenAlt().getListDepDame().size());	
		//TODO check altenative grand children
	}
	
	//TODO improve coverage @Test
	//testResolveDameWithDependenciesNotPresentInTheRepo
	
	
	
	
	
}