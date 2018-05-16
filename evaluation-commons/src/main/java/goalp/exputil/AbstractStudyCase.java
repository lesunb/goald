package goalp.exputil;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.beliefs.model.IRepository;
import goalp.evaluation.Dataset;
import goalp.evaluation.model.ExecResult;
import goalp.model.Agent;
import goalp.model.AgentBuilder;
import goalp.model.DeploymentRequest;
import goalp.model.DeploymentRequestBuilder;
import goalp.systems.DeploymentPlanningResult;
import goalp.systems.IDeploymentPlanner;
import goalp.systems.PlanSelectionException;
import goalp.systems.RepositoryBuilder;
import goalp.systems.SimpleDeploymentPlanner;

//@Named
public abstract class AbstractStudyCase {

	IDeploymentPlanner planner;
	
	IRepository repo;
	
	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;
	
	@Inject
	WriteService write;

	Dataset ds;
	

	public abstract void caseStudy();
	
	protected abstract void setupEnvironment(RepositoryBuilder repo2);

	public void exec() throws PlanSelectionException {
		//setup environment
		timer.begin();
		RepositoryBuilder repo = RepositoryBuilder.create();
		setupEnvironment(repo);
		planner = new SimpleDeploymentPlanner(repo.build());
		timer.split("setup env");
		//execute deployment planning for case study
		ds = Dataset.init("test_case", "scenario", "time");
		caseStudy();
		ds.flush();
	}
	
	public void scenario(String experimentName, Consumer<AgentBuilder> exec) {
	
		log.info("Executing experiment {}", experimentName); 
		//run execution
		timer.begin();
		
		ExecResult result = new ExecResult();
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoal("vehicle-refueling-is-assisted:0.0.1")
				.build();
		
		
		result.setRequest(request);

		AgentBuilder agentBuilder =  AgentBuilder.create();
		exec.accept(agentBuilder);
		
		Agent agent = agentBuilder.build();

		DeploymentPlanningResult planningResult;
		try {
			timer.split("setup:" + experimentName);
			planningResult = planner.doPlan(request, agent);
			Number responseResult = timer.split("execution:" + experimentName);
			ds.log(experimentName, responseResult);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		result.setResultPlan(planningResult);
		Number responseResult = timer.split("execution:" + experimentName);

		validateResult(result);
		timer.split("validation");
		echo(result.getResultPlan(), responseResult);
		timer.finish();
	}
	
	
	
	private void validateResult(ExecResult result) {
		// TODO Auto-generated method stub
		
	}
	

	public void echo(DeploymentPlanningResult resultPlan, Number time) {
		log.info("############################ Exec Result");
		log.info("# Success:" + resultPlan.isSuccessfull());
		log.info("# Time:" + time);
		log.info("# Plan Size:" + resultPlan.getPlan().getSelectedArtifacts().size());
		if(!resultPlan.isSuccessfull()){
			log.info("# Failure: " + resultPlan.getFailures().toString());
		}
		log.info("#############################################");
	}

}
