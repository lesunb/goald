package goald.dam.planning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Agent;
import goald.dam.model.Alternative;
import goald.dam.model.Bundle;
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
		updater = new DamUpdater(repo, null);
	}
	
	@Test
	public void testHCCThatDoNotChangeAlternatives() {
		
		Agent agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.build();
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
		.with("antenna_capability", "gps_capability")
		.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		List<Dame> result = repo.queryRepo(query);		
		List<Alternative> alts = result.get(0).getAlts();
		Bundle def = result.get(0).getDefinition();
		
		List<Alternative> orderAlts = updater.orderAlt(agent, alts, def);
				
		assertEquals("getPositionByGPS", orderAlts.get(0).getImpl().getIdentification());
		assertEquals("getPositionByAntenna", orderAlts.get(1).getImpl().getIdentification());
		
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
