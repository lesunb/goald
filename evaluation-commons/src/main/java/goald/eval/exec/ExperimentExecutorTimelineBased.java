package goald.eval.exec;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.evaluation.timeline.TickProducer;
import goald.evaluation.timeline.TimelineEvaluation;
import goald.evaluation.timeline.TimelineTimer;
import goald.exputil.EchoService;
import goald.model.Change;
import goald.model.ContextChange;
import goald.model.ContextChange.OP;
import goald.model.ContextCondition;
import goald.model.DeploymentPlan;
import goald.model.GoalsChangeRequest;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.monitoring.CtxMonitor;
import goald.monitoring.CtxMonitorBuilder;
import goald.planning.DameRespository;
import goald.repository.IRepository;
import goalp.eval.qualifier.TimelineBased;

@TimelineBased
public abstract class ExperimentExecutorTimelineBased implements IExperimentsExecutor<TimelineEvaluation> {

	@Inject
	Logger log;

	@Inject
	EchoService echo;
	
	protected DameRespository repo;
	
	public CtxEvaluatorBuilder initialCtx;
	
	public GoalsChangeRequestBuilder goalsChangeBuilder;
	
	public abstract List<TimelineEvaluation> caseStudy();
	
	protected abstract IRepository getRepo();

	@Override
	public List<TimelineEvaluation> exec() {
		//setup environment
		setup();
		//execute deployment planning for case study
		return caseStudy();
	}
	
	public abstract void setup();
	
	public void scenarioRepetitions(int numOfRepetitions, Integer scenario, 
			TickProducer ticker,
			Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<CtxMonitorBuilder> ctxMonitorBuilding, 
			Collection<TimelineEvaluation> evaluations, TimelineEvaluation baseEvaluation) {
		
		for(int execIndex =1; execIndex<=numOfRepetitions; execIndex++){
			TimelineEvaluation evaluation = baseEvaluation.blankCopy();
			// eval.setFactors(factors);
			scenario(scenario, execIndex, ticker, ctxBuilding,
					weightMapBuilding, goalsChangeBuilding,
					ctxMonitorBuilding, evaluation);
			evaluations.add(evaluation);
		}
	}


	public void scenario(Integer scenario, int execIndex,
			TickProducer ticker,
			Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<CtxMonitorBuilder> ctxChangesBuilding,
			TimelineEvaluation evaluation) {
			
		log.info("Executing scenario s{}, repetition {}", scenario, execIndex); 
		//run execution
		
		// agent contexts and goals
		AutonomousAgent agent = createAgent(scenario, 
				execIndex, ctxBuilding, 
				goalsChangeBuilding,
				ctxChangesBuilding,
				weightMapBuilding, evaluation);
		
		CtxMonitorBuilder ctxMonitorBuilder = CtxMonitorBuilder.create();
		ctxChangesBuilding.accept(ctxMonitorBuilder);
		CtxMonitor ctxMonitor = ctxMonitorBuilder.build();		
		evaluation.getFactors().put("scenario", scenario);

		evaluation.begin();
		// start the agent. It will deploy for the initial goals
		agent.init(repo);
				
		// context changes
		try {
			//ticks fast forward in time 
			for(Long time:ticker) {
				TimelineTimer timer = (TimelineTimer) evaluation.getTimer();
				timer.forwardTimerTo(time);
				
				if(ctxMonitor.hasChange(time)) {
					// monitor return changes since the last check
					List<ContextChange> contextChanges = ctxMonitor.getChange(time);
					for(ContextChange contextChange: contextChanges){
						//evaluation.split(execIndex, "as");
						agent.handleContextChange(contextChange);
					}
				}
			}
			agent.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		echo.it(agent.getDeployment());
	}
		
	public AutonomousAgent createAgent(Integer scenario, int _execIndex,
			Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding, 
			Consumer<CtxMonitorBuilder> ctxChangesBuilding, 
			Consumer<Map<String, Integer>> weightMapBuilding, 
			TimelineEvaluation evaluation) {
		
		return new AutonomousAgent() {
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
			public void beforeChangeGoals(GoalsChangeRequest goalsChangeRequest) {
				toogleOn("system");
				for(ContextCondition context: getAgent().getActualCtx().getCtxCollection()) {
					toogleOn(context.getLabel());
				}
				
			}
			
			@Override
			public void beforePlanningForContextChange(ContextChange change) {
				if(change.getOp() == OP.ADDED) {
					toogleOn("real_"+change.getLabel(), change.getTime());
					toogleOn(change.getLabel());
				}else if(change.getOp() == OP.REMOVED) {
					toogleOff("real_"+change.getLabel(), change.getTime());
					toogleOn(change.getLabel());
				}
			}
			
			@Override
			public void damUpdated() {
				//evaluation.split(execIndex, "dam_updated");
			}
			
			@Override
			public void onShutdown() {
				evaluation.endExec();
			}
			
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);
				//evaluation.split(execIndex, "deployment_change_planned");
			}
			
			@Override
			public void onDeploymentChange(Change change) {
				boolean status = this.getAgent().getRootDame().getIsAchievable();
				if(status) {
					toogleOn("system_available");
				}else {
					toogleOff("system_available", change.getTime());
				}
			}
			@Override
			public void onFailure(Change change) {
				toogleOff("system_available", change.getTime());
			}
			
			private void toogleOn(String label) {
				evaluation.splitToogleOn(execIndex, label);
			}
			
			private void toogleOn(String label, Long timestamp) {
				evaluation.toogleOn(execIndex, label,timestamp);
			}
			
			private void toogleOff(String label) {
				evaluation.splitToogleOff(execIndex, label);
			}
			
			private void toogleOff(String label, Long timestamp) {
				evaluation.toogleOff(execIndex, label,timestamp);
			}
		};
		

	}

}
