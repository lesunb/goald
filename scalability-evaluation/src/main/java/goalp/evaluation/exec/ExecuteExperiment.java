package goalp.evaluation.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.dam.model.ContextChange;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.util.ContextChangeBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;
import goald.eval.exec.ExecResult;
import goald.eval.exec.Execution;
import goald.eval.exec.ExperimentSetup;
import goald.eval.spec.Experiment;
import goald.exputil.EchoService;
import goald.exputil.EvalUtil;
import goald.exputil.ExperimentTimer;
import goald.exputil.WriteToFileService;
import goalp.evaluation.goals.IExecuteExperiments;
import goalp.evaluation.model.PlanningExperiment;

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
	
	private Random randomGenerator;

	String rootGoal = "br.unb.rootGoal:0.0.1";
	
	@Override
	public void accept(Experiment experiment) {
		randomGenerator = new Random();
		
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
						.addGoals(execGoals);
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
		
		List<ContextChange> changes = new ArrayList<>();

		//start the data collection
		ExecResult execResult = new ExecResult(); // agent.getExecResult();
		
		// create context changes
		for(int i = 0; i<5; i++) {
			String ctxChanged = getRandom(ctx);
			ContextChange ctxRemoved = ContextChangeBuilder.create()
					.remove(ctxChanged)
					.build();
			changes.add(ctxRemoved);
			
			ContextChange ctxAdded = ContextChangeBuilder.create()
					.add(ctxChanged)
					.build();
			changes.add(ctxAdded);
		}
		
		
		// fire agent init
		agent.init(expSetup.getRepository());
		
		// handle changes
		for(ContextChange ctxChange: changes) {
			agent.handleContextChange(ctxChange);
		}
		
		exec.getEvaluation().getFactors().put("Variability", variability);
		exec.getEvaluation().getFactors().put("Bundles Count", agent.getDeployment().getBundles().size());
		echo.it(exec);
		
		return execResult;
		
	}
	
	public String getRandom(List<String> list){
		int index = randomGenerator.nextInt(list.size());
        return list.get(index);
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
