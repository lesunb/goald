package goald.intentions; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.managers.UuidEntityManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.EntityBuilder;

import goald.beliefs.DeploymentModel;
import goald.beliefs.model.Dependency.DependencyStatus;
import goald.beliefs.model.IRepository;
import goald.beliefs.util.BundleBuilder;
import goald.beliefs.util.DeploymentModelBuilder;
import goald.beliefs.util.RepositoryBuilder;
import goald.events.DeploymentRequest;
import goald.events.utils.DeploymentRequestBuilder;
import goalp.systems.PlanSelectionException;
import net.mostlyoriginal.api.event.common.EventManager;

public class PlanDeploymentChangeTest {
	
	PlanDeploymentChange deploymentPlanningSystem;
	World world;
	IRepository	repo;
	
	@Before
	public void setUp() throws Exception {
		deploymentPlanningSystem = new PlanDeploymentChange();
		
		world = new World();
		world.setSystem(deploymentPlanningSystem);
		
		world.setManager(new EventManager());
		world.setManager(new UuidEntityManager());
		
		/** 
		 * do not call world.initialize();
		 *  it will be called by each test 
		 *  after finish the world preparation
		 */
		
		repo = RepositoryBuilder.create()
				.add(
					BundleBuilder.create()
					.identification("greater.def")
					.defines("greet")
					.build())
				.add(
					BundleBuilder.create()
					.identification("greater.impl")
					.provides("greet")
					.requires("display_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("alarm")
					.provides("alarm")
					.requires("sound_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("displayMyPosition.def")
					.defines("displayMyPosition")
					.build())
				.add(
					BundleBuilder.create()
					.identification("displayMyPosition.impl")
					.provides("displayMyPosition")
					.requires("display_capability")
					.dependsOn("getPositionByGPS")
					.dependsOn("mapView")
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPositionByGPS.def")
					.defines("getPositionByGPS")
					.build())
				.add(
					BundleBuilder.create()
					.identification("getPositionByGPS")
					.provides("getPositionByGPS")
					.requires("gps_capability")
					.build())
				.add(
					BundleBuilder.create()
					.identification("mapView.def")
					.provides("mapView")
					.build())
				.add(
					BundleBuilder.create()
					.identification("mapView.impl")
					.provides("mapView")
					.requires("display_capability")
					.build())
				.build();
	}
	
	@Test
	public void testUpdate() {
		deploymentPlanningSystem.process();
		assertTrue(true);
	}
	
	/**
	 * Handle a new 1st level simple goal 
	 */
	@Test
	public void handleSimpleRedeploymentRequest() throws PlanSelectionException {
		world.initialize();
		
		DeploymentModel depl = DeploymentModelBuilder.create()
				.build();
		
		/** Create Agent */
		Entity agent = new EntityBuilder(world)
			.with((Component) repo)
			.with(depl)
			.build();
		
		// Create a deployment request
		world.setSystem(new VoidEntitySystem() {
			
			@Wire
			EventManager em;
			
			@Override
			protected void processSystem() {
				final DeploymentRequest request = DeploymentRequestBuilder.create()
						.to(agent.getUuid())
						.addGoal("greet")
						.build();
				em.dispatch(request);
			}
		});
		
		world.process();
		assertEquals(DependencyStatus.DEPLOYED, agent.getComponent(DeploymentModel.class)
				.getDeploymentUnit().getDependencies()
				.get(0).status);
	}
}
