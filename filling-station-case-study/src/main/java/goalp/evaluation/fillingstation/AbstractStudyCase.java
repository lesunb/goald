package goalp.evaluation.fillingstation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.dam.execution.DeploymentExecutor;
import goald.dam.model.Agent;
import goald.dam.model.ContextChange;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.GoalsChangeRequest;
import goald.dam.model.util.AgentBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;
import goald.dam.planning.ContextChangeHandler;
import goald.dam.planning.DameRespository;
import goald.dam.planning.DeploymentPlanner;
import goald.dam.planning.GoalsChangeHandler;
import goald.eval.exec.ExecResult;
import goald.repository.RepositoryBuilder;
import goalp.evaluation.Dataset;
import goalp.exputil.ExperimentTimer;

//@Named
public abstract class AbstractStudyCase {

	
	DameRespository repo;
	
	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;
//	
//	@Inject
//	WriteService write;

	Dataset ds;
	

	public abstract void caseStudy();
	
	protected abstract void setupEnvironment(RepositoryBuilder repo2);

	public void exec() {
		//setup environment
		timer.begin();
		RepositoryBuilder hashRepo = RepositoryBuilder.create();
		setupEnvironment(hashRepo);
		repo = new DameRespository(hashRepo.build());
		timer.split("setup env");
		//execute deployment planning for case study
		ds = Dataset.init("test_case", "scenario", "time");
		caseStudy();
		ds.flush();
	}
	
	public void scenario(String experimentName, Consumer<AgentBuilder> agentBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<List<ContextChange>> changesBuilding) {
	
		log.info("Executing experiment {}", experimentName); 
		//run execution
		timer.begin();
		
		// construct the scenario
		//ExecResult result = new ExecResult();

		AgentBuilder agentBuilder =  AgentBuilder.create();
		agentBuilding.accept(agentBuilder);
		Agent agent = agentBuilder.build();
		
		GoalsChangeRequestBuilder goalsChangeBuilder = GoalsChangeRequestBuilder.create();
		goalsChangeBuilding.accept(goalsChangeBuilder);
		GoalsChangeRequest goalsChangeRequest = goalsChangeBuilder.build();
		
		List<ContextChange> changes = new ArrayList<>();
		changesBuilding.accept(changes);
		
		// construct the goald systems
		
		GoalsChangeHandler gcHandler = new GoalsChangeHandler(repo, agent);
		DeploymentPlanner deploymentPlanner = new DeploymentPlanner(repo, agent);
		DeploymentExecutor executor = new DeploymentExecutor(agent);
		ContextChangeHandler ccHandler = new ContextChangeHandler(repo, agent);
		
		// exec the experiment
		try {
			timer.split("setup:" + experimentName);
			gcHandler.handle(goalsChangeRequest);
			DeploymentPlan initialPlan = deploymentPlanner.createPlan();
			executor.execute(initialPlan);
			Number responseResult = timer.split("execution:" + experimentName);
			ds.log(experimentName, responseResult);
			for(ContextChange change:changes) {
				DeploymentPlan adaptPlan;
				ccHandler.handle(change);
				adaptPlan = deploymentPlanner.createPlan();
				executor.execute(adaptPlan);
				Number resultTime = timer.split("execution:" + experimentName);
				ds.log(experimentName, resultTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		log.info(agent.getRootDame().getChosenAlt().toString());
		
		timer.split("validation");
		//echo(result.getResultPlan(), responseResult);
		timer.finish();
	}

	
	private void validateResult(ExecResult result) {
		// TODO Auto-generated method stub
		
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
