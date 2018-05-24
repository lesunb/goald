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
		
		Agent agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.build();
		
		updater = new DamUpdater(repo, agent);
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
		.with("gps_capability")
		.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		Dame dame = repo.queryRepo(query).get(0);		
		
		updater.resolveDame(ctx, dame);
		
		agent.setRootDame(dame);
		
		assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
		
		ContextChange change = new ContextChange(ContextChange.OP.ADDED, "antenna_capability");
		
		ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
		
		handler.handle(change);
		
		assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
		
	}
	
	@Test
	public void handleCCFWithDropInQuality() {
		assertFalse(true);
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
