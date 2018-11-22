package goald.eval.exec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.evaluation.timeline.TickProducer;
import goald.evaluation.timeline.TimelineEvaluation;
import goald.exputil.AllPossibleChangesIterator;
import goald.exputil.AllPossibleChangesIterator.CtxChangesIterator;
import goald.exputil.EchoService;
import goald.model.Change;
import goald.model.ContextChange;
import goald.model.DeploymentPlan;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.VERespository;
import goald.repository.IRepository;
import goalp.eval.qualifier.TimelineBased;

@TimelineBased
public abstract class ExperimentExecutorAllContextsBased implements IExperimentsExecutor<TimelineEvaluation> {

	@Inject
	Logger log;

	@Inject
	EchoService echo;
	
	protected VERespository repo;
	
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
			Consumer<List<String>> contextsConsumer,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Collection<TimelineEvaluation> evaluations, 
			TimelineEvaluation baseEvaluation) {
		
		List<String> ctxs = new ArrayList<>();
		contextsConsumer.accept(ctxs);
		AllPossibleChangesIterator changesIterator = AllPossibleChangesIterator.init(ctxs);
		
		int execIndex = 0; 
		for(CtxChangesIterator ctxChangesIterator: changesIterator){
			TimelineEvaluation evaluation = baseEvaluation.blankCopy();
			// eval.setFactors(factors);
			for(ContextChange ctxChange: ctxChangesIterator.getCtxChanges()) {
				scenario(scenario, execIndex++, ticker, ctxChangesIterator.getCtxs(),
						weightMapBuilding, goalsChangeBuilding, ctxChange,
						evaluation);
				evaluations.add(evaluation);
			}
		}
	}


	public void scenario(Integer scenario, int execIndex,
			TickProducer ticker,
			List<String> initialCtxs,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			ContextChange ctxChange,
			TimelineEvaluation evaluation) {
			
		log.info("Executing scenario s{}, repetition {}", scenario, execIndex); 
		//run execution
		
		// agent contexts and goals
		AutonomousAgent agent = createAgent(scenario, 
				execIndex, initialCtxs, 
				goalsChangeBuilding,
				weightMapBuilding, evaluation);
		
		evaluation.getFactors().put("scenario", scenario);

		evaluation.begin();
		// start the agent. It will deploy for the initial goals
		agent.init(repo);
		
		agent.handleContextChange(ctxChange);
		evaluation.endExec(100);
		agent.shutdown();
		echo.it(agent.getDeployment());
	}
		
	public AutonomousAgent createAgent(Integer scenario, int _execIndex,
			List<String> initialCtxs,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding, 
			Consumer<Map<String, Integer>> weightMapBuilding, 
			TimelineEvaluation evaluation) {
		
		return new AutonomousAgent() {
			int execIndex = _execIndex;
			
			@Override
			public void setup(CtxEvaluatorBuilder _initialCtx, 
					GoalsChangeRequestBuilder _goalsChangeBuilder, 
					Map<String, Integer> weightMap){
				
				_initialCtx.with(initialCtxs);
				goalsChangeBuilding.accept(_goalsChangeBuilder);
				weightMapBuilding.accept(weightMap);
			}
			
			@Override
			public void onStartup() {
				System.out.println("--");
				System.out.println("--");
				System.out.println("exec:"+ execIndex);
			}
						
			@Override
			public void beforePlanningForContextChange(ContextChange change) {
				System.out.println(change);
			}
			
			@Override
			public void onDeploymentChangePlanned(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);
				//toogleOn("changing_deployment");
			}
			
			@Override
			public void onDeploymentChangeExecuted(Change change, DeploymentPlan adaptPlan) {
				boolean status = this.getAgent().getRootDame().isAchievable();
				
				System.out.print(status);
			}
			@Override
			public void onFailure(Change change) {
				System.out.println("failure>>>");
				System.out.println(change);
				System.out.println("<<<failure");
			}
		};
	}
}
