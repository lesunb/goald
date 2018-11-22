package goald;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.mock.FeelingStationAdvisorRepoMock;
import goald.model.CtxEvaluator;
import goald.model.Dependency;
import goald.model.GoalDManager;
import goald.model.VE;
import goald.model.util.AgentBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.RepoQueryBuilder;
import goald.planning.DVMUpdater;
import goald.planning.VERespository;

public class DependencyCondModifierTest {
	
	
	VERespository repo;
	GoalDManager managerInvalidCond, managerValidCond;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();

		CtxEvaluator soundAlertActiveWithouSound = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("sound_alert_active")
				.build();
		
		managerInvalidCond = AgentBuilder.create()
				.withContext(soundAlertActiveWithouSound)
				.build();
		
		CtxEvaluator soundAlertActiveWithSound = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("sound_alert_active")
				.with("display_capability")
				.with("sound_capability")
				.build();
		
		managerInvalidCond = AgentBuilder.create()
				.withContext(soundAlertActiveWithouSound)
				.build();
		
		managerValidCond = AgentBuilder.create()
				.withContext(soundAlertActiveWithSound)
				.build();	
	}

	@Test
	public void testInNoApplicableContext() {	
		DVMUpdater updater;
		updater = new DVMUpdater(repo, managerInvalidCond);
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("premiumDriveTips")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertTrue(result);
		Assert.assertNotNull(ve.getChosenAlt().getImpl());
		Assert.assertEquals("premiumDriveTips.impl", ve.getChosenAlt().getImpl().identification);
		Assert.assertNull("dep chosen alt should be null", ve.getChosenAlt().getListDepVE().get(0).getChosenAlt());
	}
	
	@Test
	public void testInApplicableAndDeployableContext() {
		DVMUpdater updater;
		updater = new DVMUpdater(repo, managerValidCond);
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("soundAlertWhileDriving")
				.build();
		//in not a premium driver, do not deploy, do not fail
		VE ve = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertTrue(result);
		Assert.assertNotNull(ve.getChosenAlt().getImpl());
		Assert.assertEquals("soundAlertWhileDriving.impl", ve.getChosenAlt().getImpl().identification);
		Assert.assertNotNull("dep chosen alt should not be null", ve.getChosenAlt().getListDepVE().get(0).getChosenAlt());
	}
	
	@Test
	public void testInApplicableContextThatCanNotBeResolved() {
		DVMUpdater updater;
		updater = new DVMUpdater(repo, managerInvalidCond);
		
		List<Dependency> query = RepoQueryBuilder.create()
				.queryFor("soundAlertWhileDriving")
				.build();
		
		VE ve = repo.queryRepo(query).get(0);		
		
		boolean result = updater.resolveVE(ve).isAchievable();
		Assert.assertFalse(result);
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
