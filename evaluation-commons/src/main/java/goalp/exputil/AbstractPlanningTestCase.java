package goalp.exputil;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.beliefs.model.IRepository;
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

public abstract class AbstractPlanningTestCase {

	
	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;
	
	@Inject
	ConsoleDeploymentPlanWriteService write;

	private IDeploymentPlanner planner;
	

	public void setup() {
		//setup environment
		timer.begin();
		RepositoryBuilder repo = RepositoryBuilder.create();
		insantiateRepository(repo);
		planner = new SimpleDeploymentPlanner(repo.build());
		timer.split("setup env");
	}

	protected abstract void insantiateRepository(RepositoryBuilder repo);
	
	public DeploymentPlanningResult scenario(String experimentName, Consumer<AgentBuilder> exec) {
	
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
			planningResult = planner.doPlan(request, agent);
			result.setResultPlan(planningResult);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		
		Number responseResult = timer.split("execution:" + experimentName);

		validateResult(result);
		timer.split("validation");
		echo(result.getResultPlan(), responseResult);
		timer.finish();
		return result.getResultPlan();
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
