package goald.dam.planning;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class HandleContextChangeTest {
	
	DamUpdater updater;
	DameRespository repo;
	
	@Before
	public void setup() {
		repo = FeelingStationAdvisorRepoMock.regRepo();
		updater = new DamUpdater(repo, null);
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
	public void testHCCThatDoNotChangeAlternatives() {
		assertFalse(true);
	}
	
	@Test
	public void handleCCFWithDropInQuality() {
		assertFalse(true);
	}
	
	@Test
	public void handleCCFWithIncreaseInQuality() {
		assertFalse(true);
	}
	
}
