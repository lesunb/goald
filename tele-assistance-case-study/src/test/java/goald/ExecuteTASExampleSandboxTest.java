
package goald;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import goald.analysis.DVMUpdater;
import goald.evaluation.tas.TASRepository;
import goald.model.Change;
import goald.model.ContextChange;
import goald.model.CtxEvaluator;
import goald.model.Deployment;
import goald.model.DeploymentPlan;
import goald.model.GoalDManager;
import goald.model.GoalsChangeRequest;
import goald.model.util.AgentBuilder;
import goald.model.util.ContextChangeBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.GoalsChangeHandler;
import goald.planning.VERespository;

public class ExecuteTASExampleSandboxTest {
	
	DVMUpdater updater;
	VERespository repo;
	GoalDManager agent;
	
	@Before
	public void setup() {
		repo = new VERespository(TASRepository.getRepo());
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
//				.with("gps_capability")
//				.with("antenna_capability")
//				.with("display_capability")
//				.with("sound_capability")
				.build();
			
			agent = AgentBuilder.create()
				.withQualityWeight("precision", 3)
				.withQualityWeight("responseTime", 1)
				.withContext(ctx)
				.build();

	}
	
//	@Test
//	public void testCreateAInitialPlan() {
//		
//		GoalsChangeRequest change = GoalsChangeRequestBuilder.create()
//		.addGoal("ProvideHealthSupport")
//		.build();
//		
//		GoalsChangeHandler handler = new GoalsChangeHandler(repo, agent);
//		handler.handle(change);
//				
//		assertNotNull(agent.getRootDame());
//	}
	
	@Test
	public void testReplaning() {
		scenario(
				(ctxEvaluatorBuilder)-> ctxEvaluatorBuilder
					.with(
						"internet-connection")
				,
				(qualityWeightsMap) -> {
					qualityWeightsMap.put("precision", 2);
					qualityWeightsMap.put("responseTime", 1);
					qualityWeightsMap.put("storegedSize", 1);
					qualityWeightsMap.put("usability", 1);
				},
				(goalsChangeBuilding)-> goalsChangeBuilding
					.addGoal("ProvideHealthSupport")
				,
				(changesList) -> 
					changesList.addAll(Arrays.asList(
						ContextChangeBuilder.create()
							.add("!battery-is-low").build(),
						ContextChangeBuilder.create()
							.remove("!battery-is-low").build()
						)),
				(deployment) -> {
					assertEquals(8, deployment.getBundles().size());
				}
				);

	}
	
	
	public void scenario(
			Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<List<ContextChange>> ctxChangesBuilding,
			Consumer<Deployment> deploymentConsumer) {
			
		System.out.println("Executing scenario s{}, repetition {}"); 
		//run execution
		
		// agent contexts and goals
		AutonomousAgent agent = createAgent(ctxBuilding, 
				goalsChangeBuilding,
				ctxChangesBuilding,
				weightMapBuilding);
		
		List<ContextChange> changes = new ArrayList<>();
		ctxChangesBuilding.accept(changes);		
				
		// context changes
		try {
			// start the agent. It will deploy for the initial goals
			agent.init(repo);
					
			// context changes
			try {
				for(ContextChange change:changes) {
					agent.handleContextChange(change);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
		
		deploymentConsumer.accept(agent.getAgent().getDeployment());
		System.out.println(agent.getDeployment());
	}
		

	public AutonomousAgent createAgent(
			Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding, 
			Consumer<List<ContextChange>> ctxChangesBuilding, 
			Consumer<Map<String, Integer>> weightMapBuilding) {
		
		return new AutonomousAgent() {
			
			
			@Override
			public void setup(CtxEvaluatorBuilder _initialCtx, 
					GoalsChangeRequestBuilder _goalsChangeBuilder, 
					Map<String, Integer> weightMap){
				
				ctxBuilding.accept(_initialCtx); 
				goalsChangeBuilding.accept(_goalsChangeBuilder);
				weightMapBuilding.accept(weightMap);
			}
			
			@Override
			public void onStartup() {
				System.out.println("onStartup");
			}
						
			@Override
			public void beforePlanningForContextChange(ContextChange change) {
				//System.out.println(change);
			}
			
			@Override
			public void onDeploymentChangePlanned(DeploymentPlan adaptPlan) {
				//System.out.println(adaptPlan);
				//toogleOn("changing_deployment");
			}
			
			@Override
			public void onDeploymentChangeExecuted(Change change, DeploymentPlan adaptPlan) {
				System.out.println(change);
				System.out.println(adaptPlan);
			}
			
			@Override
			public void onFailure(Change change) {
				//System.out.println(change);
			}
		};
	}
}
