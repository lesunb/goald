package goald.evaluation.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.eval.exec.Evaluation;
import goald.eval.exec.ExperimentSetup;
import goald.eval.spec.ExecSpec;
import goald.eval.spec.Experiment;
import goald.evaluation.model.PlanningExperiment;
import goald.exputil.EchoService;
import goald.exputil.ExperimentTimer;
import goald.exputil.WriteToFileService;
import goald.model.ContextChange;
import goald.model.DeploymentPlan;
import goald.model.util.ContextChangeBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;

@Named
public class ExecuteExperiment {

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
	
	public void setup(Experiment experiment) {
		randomGenerator = new Random();
		
		//preamble
		timer.begin();
		setupEnvironment((PlanningExperiment)experiment);
		timer.split("setup env");
	
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

	public Evaluation execute(ExecSpec spec, Evaluation evaluation) {
		timer.begin();
		
		// get exec params
		int numberOfGoals=0, variability = 0, numberOfChanges=0;
				
		try{
			numberOfGoals = spec.getInteger("numberOfGoals");
			numberOfChanges = (Integer) evaluation.getConstants().get( "numberOfChanges");
			variability = spec.getInteger("variability");
		}catch(NullPointerException e){
			throw new IllegalStateException("can't get number of goals or variability");
		}

		List<String> ctx = expSetup.getCtxSpace();
		
		List<String> repositoryGoals = expSetup.getRootGoals(variability);
		
		// parameterize the exec by number of goals
		List<String> execGoals = new ArrayList<>(); 
		for(int i=0; i< numberOfGoals; i++){
			execGoals.add(repositoryGoals.get(i));
		}
			
		final int execIndex = spec.getInteger("index");
				
		//create a experiment agent
		AutonomousAgent agent = new AutonomousAgent() {
			
			@Override
			public void setup(CtxEvaluatorBuilder initialCtx, 
					GoalsChangeRequestBuilder goals, Map<String, Integer> weightMap) {
					
				initialCtx.with(ctx);
				goals.addGoals(execGoals);
			}
			
			@Override
			public void changingGoals() {
				evaluation.split(execIndex, "changing_goals_" + this.version);
			}
			
			@Override
			public void damUpdated() {
				evaluation.split(execIndex, "dam_updated_" + this.version);
			}
			
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);
				evaluation.split(execIndex, "deployment_change_planned_" + this.version);
			}
			
			@Override
			public void deploymentChangeExecuted() {
				evaluation.split(execIndex, "deployment_change_excuted_" + this.version);
			}
		};
		
		List<ContextChange> changes = new ArrayList<>();


		// create context changes
		for(int i = 0; i< numberOfChanges; i++) {
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
		
		evaluation.getFactors().put("variability", variability);
		evaluation.getFactors().put("bundles_count", agent.getDeployment().getBundles().size());
		
		return evaluation;
		
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
}
