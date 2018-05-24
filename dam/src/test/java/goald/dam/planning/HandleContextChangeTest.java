package goald.dam.planning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Agent;
import goald.dam.model.ContextChange;
import goald.dam.model.Dame;
import goald.dam.model.Goal;
import goald.dam.model.util.AgentBuilder;
import goald.dam.model.util.ContextChangeBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.RepoQueryBuilder;

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
		
		ContextChange change = ContextChangeBuilder.create()
				.add("antenna_capability")
				.build();
		
		ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
		
		handler.handle(change);
		
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
		assertFalse(true);
	}
	
	@Test
	public void testHCCThatRecoveryAchivabilityOfGoals() {
		assertFalse(true);
	}
	
	
	@Test
	public void handleCCFWithIncreaseInQuality() {
		assertFalse(true);
	}
	
}
