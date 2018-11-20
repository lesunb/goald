package goald;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.model.GoalDManager;
import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.CtxEvaluator;
import goald.model.VE;
import goald.model.Goal;
import goald.model.util.AgentBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.DamUpdater;
import goald.planning.DameRespository;

public class OrderAlternativesTest {
	
	DameRespository repo;
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
	}
	

	@Test
	public void testOrderTwoAlternatives() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
		.with("antenna_capability", "gps_capability")
		.build();
		
		GoalDManager agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();
		
		List<Goal> query = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		List<VE> result = repo.queryRepo(query);		
		List<Alternative> alts = result.get(0).getAlts();
		Bundle def = result.get(0).getDefinition();
		
		DamUpdater updater = new DamUpdater(repo, agent);
		List<Alternative> orderAlts = updater.orderAlt(alts, def);
				
		assertEquals("getPositionByGPS", orderAlts.get(0).getImpl().getIdentification());
		assertEquals("getPositionByAntenna", orderAlts.get(1).getImpl().getIdentification());
		
		/* change context params */
		CtxEvaluator ctx2 = CtxEvaluatorBuilder.create()
		.with("antenna_capability", "gps_capability")
		.build();
		
		GoalDManager agent2 = AgentBuilder.create()
				.withQualityWeight("precision", 1)
				.withQualityWeight("responseTime", 3)
				.withContext(ctx2)
				.build();
		
		List<Goal> query2 = RepoQueryBuilder.create()
				.queryFor("getPosition")
				.build();
		
		List<VE> result2 = repo.queryRepo(query2);		
		List<Alternative> alts2 = result2.get(0).getAlts();
		Bundle def2 = result2.get(0).getDefinition();
		
		DamUpdater updater2 = new DamUpdater(repo, agent2);
		List<Alternative> orderAlts2 = updater2.orderAlt(alts2, def2);
				
		assertEquals("getPositionByAntenna", orderAlts2.get(0).getImpl().getIdentification());
		assertEquals("getPositionByGPS", orderAlts2.get(1).getImpl().getIdentification());
	}
}
