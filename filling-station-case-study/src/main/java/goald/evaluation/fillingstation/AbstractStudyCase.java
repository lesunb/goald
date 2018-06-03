package goald.evaluation.fillingstation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.dam.model.ContextChange;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;
import goald.dam.planning.DameRespository;
import goald.exputil.EchoService;
import goald.exputil.ExperimentTimer;
import goald.repository.IRepository;
import goald.repository.RepositoryBuilder;
import goalp.evaluation.Dataset;

//@Named
public abstract class AbstractStudyCase {

	
	DameRespository repo;
	
	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;
//	
	@Inject
	EchoService echo;

	Dataset ds;
	
	public CtxEvaluatorBuilder initialCtx;
	public GoalsChangeRequestBuilder goalsChangeBuilder;
	
	public abstract void caseStudy();
	
	protected abstract IRepository getRepo();

	public void exec() {
		//setup environment
		timer.begin();
		RepositoryBuilder hashRepo = RepositoryBuilder.create();
		repo = new DameRespository(getRepo());
		timer.split("setup env");
		//execute deployment planning for case study
		ds = Dataset.init("test_case", "scenario", "time");
		caseStudy();
		ds.flush();
	}
	
	public void scenario(String experimentName, Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<List<ContextChange>> changesBuilding) {
	
		log.info("Executing experiment {}", experimentName); 
		//run execution
		timer.begin();
		
		// agent contexts and goals
		AutonomousAgent agent = createAgent(ctxBuilding, goalsChangeBuilding);
		
		// contexts changes
		List<ContextChange> changes = new ArrayList<>();
		changesBuilding.accept(changes);

		// start the agent. It will deploy for the initial goals
		agent.init(repo);
				
		// context changes
		try {
			timer.split("start_addapting:" + experimentName);
			for(ContextChange change:changes) {
				agent.handleContextChange(change);
				Number resultTime = timer.split("handling_change:" + experimentName);
				ds.log(experimentName, resultTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		// log.info(agent.getRootDame().getChosenAlt().toString());
		
		timer.split("validation");
		//echo(result.getResultPlan(), responseResult);
		timer.finish();
	}
		
	public AutonomousAgent createAgent(Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding) {
		return new AutonomousAgent() {
			@Override
			public void setup(CtxEvaluatorBuilder _initialCtx, 
					GoalsChangeRequestBuilder _goalsChangeBuilder) {
				ctxBuilding.accept(_initialCtx); 
				goalsChangeBuilding.accept(_goalsChangeBuilder); 
			}
			
			@Override
			public void changingGoals() {
				timer.split("changing_goals");
			}
			
			@Override
			public void damUpdated() {
				timer.split("dam_updated");
			}
			
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);
				timer.split("deployment_change_planned");
			}
			
			@Override
			public void deploymentChangeExecuted() {
				timer.split("deployment_change_excuted");
			}
		};
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
