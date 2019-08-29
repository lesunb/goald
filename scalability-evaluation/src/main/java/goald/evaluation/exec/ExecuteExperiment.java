package goald.evaluation.exec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.eval.exec.ExperimentSetup;
import goald.eval.spec.ExecSpec;
import goald.eval.spec.Experiment;
import goald.evaluation.model.PlanningExperiment;
import goald.evaluation.response.ResponseEvaluation;
import goald.exputil.EchoService;
import goald.exputil.ExperimentTimer;
import goald.exputil.WriteToFileService;
import goald.model.Change;
import goald.model.ContextChange;
import goald.model.DeploymentPlan;
import goald.model.GoalsChangeRequest;
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

		// preamble
		timer.begin();
		setupEnvironment((PlanningExperiment) experiment);
		timer.split("setup_env");

	}

	private void setupEnvironment(PlanningExperiment exp) {
		log.debug("setup repo :" + exp.getName() + ":" + exp.toString());

		expSetup = new ExperimentSetup();

		// create repo and set in the expSetup

		PrismRepositoryFactory.create().buildBySpec(exp.getRepoSpec()).setSetupWithRepo(expSetup)
				.setSetupRootGoals(expSetup).setCtxSpace(expSetup);

		echo.it(expSetup);
	}

	public class ExecSetup {

		private List<String> execGoals;
		private List<ContextChange> changes;
		private List<String> ctx;

		public ExecSetup(List<String> execGoals, List<String> ctx, List<ContextChange> changes) {
			this.execGoals = execGoals;
			this.changes = changes;
			this.ctx = ctx;
		}

		public List<String> getExecGoals() {
			return execGoals;
		}

		public List<String> getCtx() {
			return this.ctx;
		}

		public List<ContextChange> getChanges() {
			return changes;
		}
	}

	public class SetupAndEvaluation {
		private ExecSetup execSetup;
		private Map<String, Long> measures;
		private ResponseEvaluation evaluation;
		private long execId;

		public SetupAndEvaluation(Map<String, String> measures, ExecSetup execSetup, ResponseEvaluation evaluation, long execId) {
			this.measures = new HashMap<>();
			this.execSetup = execSetup;
			measures.keySet().forEach(key ->{
				this.measures.put(key, 
					Long.parseLong(measures.get(key)));
			});
			this.evaluation = evaluation;
			this.execId =execId;
		}

		public long getExecId() {
			return this.execId;
		}
		
		public ExecSetup getExecSetup() {
			return execSetup;
		}
		
		public Map<String, Long> getMeasures(){
			return this.measures;
		}
		
		public ResponseEvaluation getEvaluation() {
			return this.evaluation;
		}
	}

	public ExecSetup createSetup(ExecSpec spec, ResponseEvaluation evaluation) {
		// get exec params
		int numberOfGoals = 0, variability = 0, numberOfChanges = 0;

		try {
			numberOfGoals = spec.getInteger("numberOfGoals");
			numberOfChanges = (Integer) evaluation.getConstants().get("numberOfChanges");
			variability = spec.getInteger("variability");
		} catch (NullPointerException e) {
			throw new IllegalStateException("can't get number of goals or variability");
		}

		List<String> ctx = expSetup.getCtxSpace();

		List<String> repositoryGoals = expSetup.getRootGoals(variability);

		// parameterize the exec by number of goals
		List<String> execGoals = new ArrayList<>();
		for (int i = 0; i < numberOfGoals; i++) {
			execGoals.add(repositoryGoals.get(i));
		}

		List<ContextChange> changes = new ArrayList<>();

		// create context changes
		for (int i = 0; i < numberOfChanges; i++) {
			String ctxChanged = getRandom(ctx);
			ContextChange ctxRemoved = ContextChangeBuilder.create().remove(ctxChanged).build();
			changes.add(ctxRemoved);

			ContextChange ctxAdded = ContextChangeBuilder.create().add(ctxChanged).build();
			changes.add(ctxAdded);
		}

		return new ExecSetup(execGoals, ctx, changes);
	}

	public SetupAndEvaluation execute(ExecSetup execSetup, ResponseEvaluation evaluation, 
			final int execIndex, final long execId) {
		evaluation.begin();
		AutonomousAgent manager = createManager(execSetup, evaluation, execIndex);
		evaluation.putFactor("execId", execId);
		evaluation.split(execIndex, "exp_exec_setup");
		
		// fire agent init
		manager.init(expSetup.getRepository());
		
		evaluation.putFactor("bundles_count", manager.getDeployment().getBundles().size());
	
		// handle changes
		for (ContextChange ctxChange : execSetup.getChanges()) {
			manager.handleContextChange(ctxChange);
		}

		manager.shutdown();

		return new SetupAndEvaluation(evaluation.getMeasuresMap(execIndex), execSetup, evaluation, execId);
	}
	
	public String getRandom(List<String> list) {
		int index = randomGenerator.nextInt(list.size());
		return list.get(index);
	}

	public AutonomousAgent createManager(ExecSetup execSetup, ResponseEvaluation evaluation, final int execIndex) {

		// create a experiment agent
		return new AutonomousAgent() {

			@Override
			public void setup(CtxEvaluatorBuilder initialCtx, GoalsChangeRequestBuilder goals,
					Map<String, Integer> weightMap) {

				initialCtx.with(execSetup.getCtx());
				goals.addGoals(execSetup.getExecGoals());
			}

			@Override
			public void beforeChangeGoals(GoalsChangeRequest goalsChangeRequest) {
				evaluation.split(execIndex, "changing_goals_" + this.version);
			}

			@Override
			public void damUpdated() {
				evaluation.split(execIndex, "dam_updated_" + this.version);
			}

			@Override
			public void afterPlanningForContextChange(ContextChange change, boolean result) {
				evaluation.putFactor("deployment_change_status_" + this.version, result ? 1 : 0);
			}

			@Override
			public void onDeploymentChangePlanned(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);

				evaluation.split(execIndex, "deployment_change_planned_" + this.version);
				evaluation.putFactor("deployment_change_size_" + this.version, adaptPlan.getCommands().size());
			}

			@Override
			public void onDeploymentChangeExecuted(Change change, DeploymentPlan adaptPlan) {
				evaluation.split(execIndex, "deployment_change_excuted_" + this.version);
			}

			// @Override
			// public void onShutdown() {
			// System.gc();
			// }
		};
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
