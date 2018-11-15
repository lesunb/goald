package goald.eval.exec;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.evaluation.TickerTimer;
import goald.evaluation.timeline.TimelineEvaluation;
import goald.exputil.EchoService;
import goald.model.ContextChange;
import goald.model.DeploymentPlan;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.monitoring.CtxMonitor;
import goald.monitoring.CtxMonitorBuilder;
import goald.planning.DameRespository;
import goald.repository.IRepository;
import goalp.eval.qualifier.TimelineBased;

@TimelineBased
public abstract class TimelineBasedExperimentExecutor implements IExperimentsExecutor<TimelineEvaluation> {

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
		//evaluation.split(execIndex, "initing_agent");
		// start the agent. It will deploy for the initial goals
		agent.init(repo);
				
		// context changes
		try {
			//evaluation.split(execIndex, "start_addapting");

			//ticks fast forward in time 
			for(Long time:ticker) {
				
				TickerTimer timer = (TickerTimer) evaluation.getTimer();
				timer.forwardTimerTo(time);
				
				if(ctxMonitor.hasChange(time)) {
					// monitor return changes since the last check
					List<ContextChange> contextChanges = ctxMonitor.getChange(time);
					
					for(ContextChange contextChange: contextChanges){
						//evaluation.split(execIndex, "as");
						
						agent.beforeHandleContextChange(contextChange);
						agent.handleContextChange(contextChange);
						agent.afterHandleContextChange(contextChange);
					}
				}
				//evaluation.split(execIndex, "handling_change");
			}
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
			public void changingGoals() {
				//evaluation.split(execIndex, "changing_goals");
			}
			
			@Override
			public void beforeHandleContextChange(ContextChange change) {
				//evaluation.split(execIndex, "changing_context");
			}
			
			@Override
			public void damUpdated() {
				//evaluation.split(execIndex, "dam_updated");
			}
			
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);
				//evaluation.split(execIndex, "deployment_change_planned");
			}
			
			@Override
			public void deploymentChangeExecuted() {
				//evaluation.split(execIndex, "deployment_change_excuted");
			}
		};
	}
}
