package goald.evaluation.fillingstation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.exputil.EchoService;
import goald.exputil.ExperimentTimer;
import goald.model.ContextChange;
import goald.model.DeploymentPlan;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.DameRespository;
import goald.repository.IRepository;
import goalp.evaluation.Dataset;

public abstract class AbstractStudyCase {

	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;

	@Inject
	EchoService echo;

	Dataset ds;
	
	DameRespository repo;
	
	public CtxEvaluatorBuilder initialCtx;
	public GoalsChangeRequestBuilder goalsChangeBuilder;
	
	public abstract void caseStudy();
	
	protected abstract IRepository getRepo();

	
	public void exec() {
		//setup environment
		timer.begin();
		repo = new DameRespository(getRepo());
		timer.split("setup env");
		//execute deployment planning for case study
		ds = Dataset.init("test_case", "scenario", "exec_index", "operation", "time");
		caseStudy();
		ds.flush();
	}
	
	public void scenario(String experimentName, int execIndex, Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<List<ContextChange>> changesBuilding) {
	
		log.info("Executing experiment {}", experimentName); 
		//run execution
		timer.begin();
		
		// agent contexts and goals
		AutonomousAgent agent = createAgent(experimentName, execIndex, ctxBuilding, goalsChangeBuilding, weightMapBuilding);
		
		// contexts changes
		List<ContextChange> changes = new ArrayList<>();
		changesBuilding.accept(changes);

		timer.split("initing_agent:" + experimentName);
		// start the agent. It will deploy for the initial goals
		agent.init(repo);
				
		// context changes
		try {
			timer.split("start_addapting:" + experimentName);
			for(ContextChange change:changes) {
				agent.handleContextChange(change);
				split(experimentName, execIndex, "handling_change");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
				
		timer.split("validation");
		echo.it(agent.getDeployment());
		timer.finish();
	}
		
	public AutonomousAgent createAgent(String _experimentName, int _execIndex, Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding, 
			Consumer<Map<String, Integer>> weightMapBuilding) {
		return new AutonomousAgent() {
			String experimentName = _experimentName;
			int execIndex = _execIndex;
			
			@Override
			public void setup(CtxEvaluatorBuilder _initialCtx, 
					GoalsChangeRequestBuilder _goalsChangeBuilder, 
					Map<String, Integer> weightMap){
				
				ctxBuilding.accept(_initialCtx); 
				goalsChangeBuilding.accept(_goalsChangeBuilder);
				weightMapBuilding.accept(weightMap);
			}
			
			@Override
			public void changingGoals() {
				split(experimentName, execIndex, "changing_goals");
			}
			
			@Override
			public void damUpdated() {
				split(experimentName, execIndex, "dam_updated");
			}
			
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);
				split(experimentName, execIndex, "deployment_change_planned");
			}
			
			@Override
			public void deploymentChangeExecuted() {
				split(experimentName, execIndex, "deployment_change_excuted");
			}
		};
	}

	public void split(String scenario, int execIndex, String label) {
		ds.log(scenario, execIndex, label, timer.split(label));
	}

//	public void echo(DeploymentPlanningResult resultPlan, Number time) {
//		log.info("############################ Exec Result");
//		log.info("# Success:" + resultPlan.isSuccessfull());
//		log.info("# Time:" + time);
//		log.info("# Plan Size:" + resultPlan.getPlan().getSelectedArtifacts().size());
//		if(!resultPlan.isSuccessfull()){
//			log.info("# Failure: " + resultPlan.getFailures().toString());
//		}
//		log.info("#############################################");
//	}

}
