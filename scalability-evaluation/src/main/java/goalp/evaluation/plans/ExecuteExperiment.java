package goalp.evaluation.plans;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import goald.intentions.ValidatePlan;
import goald.intentions.ValidatePlan.ValidationResult;
import goalp.evaluation.goals.IExecuteExperiments;
import goalp.evaluation.model.ExecResult;
import goalp.evaluation.model.Execution;
import goalp.evaluation.model.Experiment;
import goalp.evaluation.model.ExperimentSetup;
import goalp.evaluation.model.PlanningExperiment;
import goalp.exputil.EvalUtil;
import goalp.exputil.ExperimentTimer;
import goalp.exputil.WriteToFileService;
import goalp.model.Agent;
import goalp.model.AgentBuilder;
import goalp.model.DeploymentRequest;
import goalp.model.DeploymentRequestBuilder;
import goalp.systems.DeploymentPlanningResult;
import goalp.systems.IDeploymentPlanner;
import goalp.systems.PlanSelectionException;
import goalp.systems.SimpleDeploymentPlanner;

@Named
public class ExecuteExperiment implements IExecuteExperiments {

	@Inject
	Logger log;
	
	@Inject
	ExperimentTimer timer;
	
	@Inject
	EchoService echo;
	
	@Inject
	WriteToFileService write;
	
	ValidatePlan validate = new ValidatePlan();
	
	ExperimentSetup expSetup;

	String rootGoal = "br.unb.rootGoal:0.0.1";
	
	@Override
	public void accept(Experiment experiment) {
		
		// TODO: make it beautiful
		//	And
		//	.exec(setupEnvironmen, experiment)
		//	.exec(executeAndMesureExperiment(setup))
		
		//preamble
		timer.begin();
		setupEnvironment((PlanningExperiment)experiment);
		timer.split("setup env");
		
		log.info("Experiment factor {}", EvalUtil.getFactors((PlanningExperiment)experiment));
		//exec
		experiment.getExecutions().forEach((exec) -> {
			//TODO change for dispatch event in experiment context?
			
			//run execution
			timer.begin();
			ExecResult result = execute(exec);
			Number responseResult = timer.split("execution");
			write.it(result);
			
			validateResult(result);
			timer.split("validation");
			exec.getEvaluation().setResponseValue(responseResult);
			timer.finish();
		});
		clean();
	}

	private void setupEnvironment(PlanningExperiment exp) {
		log.debug("setup repo :" + exp.getName() + ":" + exp.toString());
		
		//TODO choose the correct strategy
		//IRepository repo = CreateSpecifiedRepository.exec(exec.getSpecification(), rootGoal);
		
		expSetup = new ExperimentSetup();
		
		PrismRepositoryBuilder
			.create()
			.buildBySpec(exp.getRepoSpec())
			.setSetupWithRepo(expSetup)
			.setSetupRootGoals(expSetup);
		
		IDeploymentPlanner planner = new SimpleDeploymentPlanner(expSetup.getRepository());
		
		List<String> agentContexts = exp.getRepoSpec().getStrList("agentContext");
		Agent agent = AgentBuilder.create()
				.addContexts(agentContexts)
				.build();

		expSetup.setPlanner(planner);
		expSetup.setAgent(agent);
		
		echo.it(expSetup);
	}

	private ExecResult execute(Execution exec) {
		
		int numberOfGoals=0, variability = 0;
		try{
			numberOfGoals = exec.getSpecification().getInteger("numberOfGoals");
			variability = exec.getSpecification().getInteger("variability");
		}catch(NullPointerException e){
			throw new IllegalStateException("can't get number of goals or variability");
		}

		List<String> repositoryGoals = expSetup.getRootGoals(variability);
		
		List<String> execGoals = new ArrayList<>(); 
		for(int i=0; i< numberOfGoals; i++){
			execGoals.add(repositoryGoals.get(i));
		}
		
		DeploymentRequest request = DeploymentRequestBuilder.create()
				.addGoals(execGoals)
				.build();
		
		DeploymentPlanningResult resultPlan = null;
		try {
			resultPlan = expSetup.getPlanner().doPlan(request,  expSetup.getAgent());
		} catch (PlanSelectionException e) {
			log.error("error planning:" + e.getMessage());
			log.error("for " + request.toString());
			log.error("with setup " + expSetup.toString());
		}
		exec.getEvaluation().getFactors().put("Variability", variability);
		exec.getEvaluation().getFactors().put("Plan size", resultPlan.getPlanSize());
		
		echo.it(exec, resultPlan);
		
		return new ExecResult(request, resultPlan);
		
	}
	

	private void validateResult(ExecResult execResult) {
		if(!execResult.getResultPlan().isSuccessfull()){
			log.error("Planning fail");
			log.error(execResult.getResultPlan().failures.toString());
			//throw new IllegalStateException("Planning fail");	
		}
		
		ValidationResult valResult = validate.exec(execResult.getRequest(), execResult.getResultPlan());
		if(execResult.getResultPlan().isSuccessfull() 
				&& valResult != ValidatePlan.ValidationResult.OK){
			log.error("Planning succeded but returned a invallid result");
			throw new IllegalStateException("Planning succeded but returned a invallid result");
		}
		
	}
	
	private void clean(){
		this.expSetup = null;
		System.gc();
	}

}
