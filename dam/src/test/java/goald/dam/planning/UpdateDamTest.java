package goald.dam.planning;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Alternative;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.util.AlternativeBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.RepoQueryBuilder;

public class UpdateDamTest {
	
	DamUpdater updater;
	DameRespository repo;
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		updater = new DamUpdater(repo, null);
	}
	

	@Test
	public void testResolveIfLeaf() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
		.with("C1")
		.build();
		
		Alternative alt = new Alternative();
		
		boolean result = updater.resolveAlt(null, ctx, alt);
		
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
		
		boolean result = updater.resolveAlt(null, ctx, alt);
		
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
		
		boolean result = updater.resolveAlt(null, ctx, alt);
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void testResolveDameInNoValidAlternative() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("C1", "C2", "C3")
				.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("alarm")
				.build();
		
		Dame dame = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveDame(null, ctx, dame);
		Assert.assertFalse(result);
		Assert.assertNull(dame.getChosenAlt());	
	}
	
	@Test
	public void testResolveDameOneValidAlternative() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		Dame dame = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveDame(null, ctx, dame);
		Assert.assertTrue(result);
		
		Assert.assertNotNull(dame.getChosenAlt());
		Assert.assertEquals(true, dame.getChosenAlt().getResolved());	
		
		CtxEvaluator ctx2 = CtxEvaluatorBuilder.create()
				.with("antenna_capability")
				.build();
		
		Dame dame2 = repo.queryRepo(query).get(0);		
		
		boolean result2 = updater.resolveDame(null, ctx2, dame2);
		Assert.assertTrue(result2);
		
		Assert.assertNotNull(dame2.getChosenAlt());
		Assert.assertEquals(true, dame2.getChosenAlt().getResolved());	
	}
	
	@Test
	public void testResolveDameMultipleAlternativeDependencies() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability", "display_capability")
				.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("displayMyPosition")
				.build();
		
		Dame dame = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveDame(null, ctx, dame);
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
