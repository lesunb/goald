package goald.dam.planning;

import static org.junit.Assert.assertEquals;

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

public class OrderAlternativesTest {
	
	DamUpdater updater;
	DameRespository repo;
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		updater = new DamUpdater(repo, null);
	}
	

	@Test
	public void testOrderTwoAlternatives() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
		.with("antenna_capability", "gps_capability")
		.build();
		
		Agent agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
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
		
		/* change context params */
		CtxEvaluator ctx2 = CtxEvaluatorBuilder.create()
		.with("antenna_capability", "gps_capability")
		.build();
		
		Agent agent2 = AgentBuilder.create()
				.withQualityWeight("precision", 1)
				.withQualityWeight("responseTime", 3)
				.build();
		
		List<Goal> query2 = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		List<Dame> result2 = repo.queryRepo(query2);		
		List<Alternative> alts2 = result2.get(0).getAlts();
		Bundle def2 = result2.get(0).getDefinition();
		
		List<Alternative> orderAlts2 = updater.orderAlt(agent2, alts2, def2);
				
		assertEquals("getPositionByAntenna", orderAlts2.get(0).getImpl().getIdentification());
		assertEquals("getPositionByGPS", orderAlts2.get(1).getImpl().getIdentification());
	}
	
}
