package goald;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.model.Agent;
import goald.model.ContextChange;
import goald.model.CtxEvaluator;
import goald.model.Dame;
import goald.model.Goal;
import goald.model.util.AgentBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.ContextChangeHandler;
import goald.planning.DamUpdater;
import goald.planning.DameRespository;

public class HandleContextChangeTest {
	
	DamUpdater updater;
	DameRespository repo;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
	}
	
	@Test
	public void testHCCThatDoNotChangeAlternatives() {
				
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
		
		updater = new DamUpdater(repo, agent);
	
		Dame dame = updater.resolveGoals(query).get(0);		
				
		agent.setRootDame(dame);
		
		assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
		
		// adaptation 

		ContextChange change2 = ContextChangeBuilder.create()
			.add("antenna_capability")
			.build();
		
		ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
		handler.handle(change2);

		Dame dame2 = repo.queryRepo(query).get(0);

		updater.resolveDame(dame2);
		
		agent.setRootDame(dame2);
		
		assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );		
	}
	
	@Test
	public void handleCCFWithDropInQuality() {
			CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("antenna_capability")
				.build();
			
			Agent agent = AgentBuilder.create()
				.withContext(ctx)
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.build();
				
			List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
			
			updater = new DamUpdater(repo, agent);
		
			Dame dame = updater.resolveGoals(query).get(0);		
					
			agent.setRootDame(dame);
			
			assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
			
			ContextChange change = ContextChangeBuilder.create()
					.remove("gps_capability")
					.build();
			
			ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
			
			handler.handle(change);
			
			assertEquals("getPositionByAntenna", agent.getRootDame().getChosenAlt().getImpl().identification );
			
	}
	
	
	@Test
	public void testHCCThatMakesTheGoalNotAchivable() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
			
			Agent agent = AgentBuilder.create()
				.withContext(ctx)
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.build();
				
			List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
			
			updater = new DamUpdater(repo, agent);
		
			Dame dame = updater.resolveGoals(query).get(0);		
					
			agent.setRootDame(dame);
			
			assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
			
			ContextChange change = ContextChangeBuilder.create()
					.remove("gps_capability")
					.build();
			
			ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
			
			boolean result = handler.handle(change);
			
			assertFalse(result);
	}
	
	@Test
	public void testHCCThatRecoveryAchivabilityOfGoals() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.build();
			
			Agent agent = AgentBuilder.create()
				.withContext(ctx)
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.build();
				
			List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
			
			updater = new DamUpdater(repo, agent);
		
			Dame dame = updater.resolveGoals(query).get(0);		
					
			agent.setRootDame(dame);
			
			assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
			
			ContextChange change = ContextChangeBuilder.create()
					.remove("gps_capability")
					.build();
			
			ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
			
			boolean result = handler.handle(change);
			
			assertFalse(result);
			
			ContextChange change2 = ContextChangeBuilder.create()
					.add("antenna_capability")
					.build();
			 
			handler.handle(change2);
			assertEquals("getPositionByAntenna", agent.getRootDame().getChosenAlt().getImpl().identification );
	}
	
	
	@Test
	public void handleCCFWithIncreaseInQuality() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("antenna_capability")
				.build();
			
			Agent agent = AgentBuilder.create()
				.withContext(ctx)
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.build();
				
			List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
			
			updater = new DamUpdater(repo, agent);
		
			Dame dame = updater.resolveGoals(query).get(0);		
					
			agent.setRootDame(dame);
			
			assertEquals("getPositionByAntenna", agent.getRootDame().getChosenAlt().getImpl().identification );
			
			ContextChange change = ContextChangeBuilder.create()
					.add("gps_capability")
					.build();
			
			ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
			
			handler.handle(change);
			assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
	}
	
}
