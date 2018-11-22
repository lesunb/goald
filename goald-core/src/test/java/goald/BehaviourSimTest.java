package goald;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.behaviour.BehaviourSim;
import goald.behaviour.CallFailure;
import goald.behaviour.Profile;
import goald.behaviour.ProfileBuilder;
import goald.mock.FeelingStationAdvisorRepoMock;
import goald.model.ContextChange;
import goald.model.CtxEvaluator;
import goald.model.Dependency;
import goald.model.GoalDManager;
import goald.model.VE;
import goald.model.util.AgentBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.ContextChangeHandler;
import goald.planning.DVMUpdater;
import goald.planning.VERespository;

public class BehaviourSimTest {
	
	DVMUpdater updater;
	VERespository repo;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
	}
	
	@Test
	public void testHCCThatDoNotChangeAlternatives() throws CallFailure {
				
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
		
		updater = new DVMUpdater(repo, agent);
	
		VE dame = updater.resolveDepenencies(query).get(0);		
				
		agent.setRootDame(dame);
		
		assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
		
		// adaptation 

		ContextChange change2 = ContextChangeBuilder.create()
			.add("antenna_capability")
			.build();
		
		ContextChangeHandler handler = new ContextChangeHandler(repo, agent);
		handler.handle(change2);

		VE dame2 = repo.queryRepo(query).get(0);

		updater.resolveVE(dame2);
		
		agent.setRootDame(dame2);
				
		assertEquals("getPositionByGPS", agent.getRootDame().getChosenAlt().getImpl().identification );
		
		Profile profile = ProfileBuilder.create()
				.withFailureRate("getPositionByGPS", 0.5f)
				.build();
		
		BehaviourSim sim = new BehaviourSim(agent, profile);
		sim.call(dame2, null);
	}
}
