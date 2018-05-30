package goalp.evaluation.exec;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;
import goald.eval.exec.ExecResult;
import goald.eval.exec.Execution;
import goald.eval.exec.ExperimentSetup;
import goald.eval.spec.Experiment;
import goalp.evaluation.goals.IExecuteExperiments;
import goalp.evaluation.model.PlanningExperiment;
import goalp.exputil.EvalUtil;
import goalp.exputil.ExperimentTimer;
import goalp.exputil.WriteToFileService;

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
	
	// ValidatePlan validate = new ValidatePlan();
	
	ExperimentSetup expSetup;

	String rootGoal = "br.unb.rootGoal:0.0.1";
	
	@Override
	public void accept(Experiment experiment) {
		
		//preamble
		timer.begin();
		setupEnvironment((PlanningExperiment)experiment);
		timer.split("setup env");
		
		log.info("Experiment factor {}", EvalUtil.getFactors(experiment));
		//exec
		experiment.getExecutions().forEach((exec) -> {
			//TODO change for dispatch event in experiment context?
			
			//run execution
			timer.begin();
			ExecResult result = execute(exec);
			Number responseResult = timer.split("execution");
			write.it(result);
			
			// validateResult(result);
			timer.split("validation");
			exec.getEvaluation().setResponseValue(responseResult);
			timer.finish();
		});
		clean();
	}

	private void setupEnvironment(PlanningExperiment exp) {
		log.debug("setup repo :" + exp.getName() + ":" + exp.toString());

		expSetup = new ExperimentSetup();

		// create repo and set in the expSetup
		
		PrismRepositoryFactory
			.create()
			.buildBySpec(exp.getRepoSpec())
			.setSetupWithRepo(expSetup)
			.setSetupRootGoals(expSetup)
			.setCtxSpace(expSetup);
				
		echo.it(expSetup);
	}

	private ExecResult execute(Execution exec) {
		timer.begin();
		
		// get exec params
		int numberOfGoals=0, variability = 0;
		
		try{
			numberOfGoals = exec.getSpecification().getInteger("numberOfGoals");
			variability = exec.getSpecification().getInteger("variability");
		}catch(NullPointerException e){
			throw new IllegalStateException("can't get number of goals or variability");
		}

		List<String> ctx = expSetup.getCtxSpace();
		List<String> repositoryGoals = expSetup.getRootGoals();
		
		// parameterize the exec by number of goals
		List<String> execGoals = new ArrayList<>(); 
		for(int i=0; i< numberOfGoals; i++){
			execGoals.add(repositoryGoals.get(i));
		}
		
		//create a experiment agent
		AutonomousAgent agent = new AutonomousAgent() {
		
			@Override
			public void setup(CtxEvaluatorBuilder initialCtx, 
					GoalsChangeRequestBuilder goals) {
					
						initialCtx.with(ctx);
						
						goals
						.addGoal(execGoals.get(0));
				timer.split("creating_dam");
				
			}
			@Override
			public void damUpdated() {
				timer.split("dam_updated");
				
			}
			
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				//echo.it(adaptPlan);
				timer.split("deployment_change_planned");
			}
			
			
			@Override
			public void deploymentChangeExecuted() {
				timer.split("deployment_change_excuted");
			}
		};
		
		// fire agent init
		agent.init(expSetup.getRepository());
		
		//start the data collection
		ExecResult execResult = new ExecResult(); // agent.getExecResult();
		
		//TODO: sequence of adaptation
		
		exec.getEvaluation().getFactors().put("Variability", variability);
		exec.getEvaluation().getFactors().put("Bundles Count", agent.getDeployment().getBundles().size());
		echo.it(exec);
		
		return execResult;
		
	}
	
//
//	private void validateResult(ExecResult execResult) {
//		if(!execResult.getResultPlan().isSuccessfull()){
//			log.error("Planning fail");
//			log.error(execResult.getResultPlan().failures.toString());
//			//throw new IllegalStateException("Planning fail");	
//		}
//		
//		ValidationResult valResult = validate.exec(execResult.getRequest(), execResult.getResultPlan());
//		if(execResult.getResultPlan().isSuccessfull() 
//				&& valResult != ValidatePlan.ValidationResult.OK){
//			log.error("Planning succeded but returned a invallid result");
//			throw new IllegalStateException("Planning succeded but returned a invallid result");
//		}
//		
//	}
	
	private void clean(){
		this.expSetup = null;
		System.gc();
	}

}