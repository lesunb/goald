package goald.eval.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.evaluation.assertives.AssertDeployment;
import goald.evaluation.assertives.AssertDeploymentBuilder;
import goald.evaluation.assertives.Assertion;
import goald.evaluation.assertives.Assertion.AssertionResult;
import goald.evaluation.assertives.Propositions;
import goald.evaluation.response.ResponseEvaluation;
import goald.exputil.AllPossibleChangesIterator;
import goald.exputil.AllPossibleChangesIterator.CtxChangesIterator;
import goald.exputil.EchoService;
import goald.model.Change;
import goald.model.ContextChange;
import goald.model.Deployment;
import goald.model.DeploymentPlan;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.VERespository;
import goald.repository.IRepository;
import goalp.eval.qualifier.TimelineBased;

@TimelineBased
public abstract class ExperimentAssertDeploymentChangesBased implements IExperimentsExecutor<ResponseEvaluation> {

	@Inject
	Logger log;

	@Inject
	EchoService echo;
	
	protected VERespository repo;
	
	public CtxEvaluatorBuilder initialCtx;
	
	public GoalsChangeRequestBuilder goalsChangeBuilder;
	
	public abstract List<ResponseEvaluation> caseStudy();
	
	protected abstract IRepository getRepo();

	@Override
	public List<ResponseEvaluation> exec() {
		//setup environment
		setup();
		//execute deployment planning for case study
		return caseStudy();
	}
	
	public abstract void setup();
	
	public void scenarioRepetitions(int numOfRepetitions, Integer scenario, 
			Consumer<List<String>> contextsConsumer,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,

			BiConsumer<AssertDeploymentBuilder,Propositions> assertDeploymentBuilding,
			BiConsumer<Assertion, Integer> assertionConsumer,
			List<ResponseEvaluation> evaluations, 
			ResponseEvaluation baseEvaluation) {
		
		List<String> ctxs = new ArrayList<>();
		contextsConsumer.accept(ctxs);
		
		AssertDeploymentBuilder assDeplbuilder = AssertDeploymentBuilder.create();
		assertDeploymentBuilding.accept(assDeplbuilder, AssertDeploymentBuilder.promiseAux());
		AssertDeployment assertDeployment = assDeplbuilder.build();
		
		AllPossibleChangesIterator changesIterator = AllPossibleChangesIterator.init(ctxs);
		
		int execIndex = 0;
		
		for(CtxChangesIterator ctxChangesIterator: changesIterator){
			// eval.setFactors(factors);
			for(ContextChange ctxChange: ctxChangesIterator.getCtxChanges()) {
				ResponseEvaluation evaluation = baseEvaluation.blankCopy();
				evaluation.putFactor("scenario", scenario);
				evaluation.getTimer().begin();
				scenario(scenario, execIndex++, ctxChangesIterator.getCtxs(),
						weightMapBuilding, goalsChangeBuilding, ctxChange,
						assertDeployment, assertionConsumer, evaluation);
				evaluations.add(evaluation);
			}
		}
	}


	public void scenario(Integer scenario, int execIndex,

			List<String> initialCtxs,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			ContextChange ctxChange,
			AssertDeployment assertDeployment,
			BiConsumer<Assertion, Integer> assertionConsumer,
			ResponseEvaluation evaluation) {
			
		log.info("Executing scenario s{}, repetition {}", scenario, execIndex); 
		//run execution
		evaluation.begin();
		// agent contexts and goals
		AutonomousAgent agent = createAgent(scenario, 
				execIndex, initialCtxs, 
				goalsChangeBuilding,
				weightMapBuilding, evaluation);
		
		
		// start the agent. It will deploy for the initial goals
		agent.init(repo);
		Deployment initialDeployment = agent.getDeployment().clone();
		
		evaluation.split(execIndex, "starting");
		agent.handleContextChange(ctxChange);
		evaluation.split(execIndex, "replanned");
		
		Deployment finalDeployment = agent.getDeployment();
		
		Assertion assertion = assertDeployment.assertChange(agent.getAgent().getActualCtx(), 
				initialDeployment, finalDeployment);
		
		int result = assertion.getResult() == AssertionResult.TRUE_POSITIVE ? 1 : 
				assertion.getResult() == AssertionResult.TRUE_NEGATIVE ? 0 
						: -1; 
		
		evaluation.putFactor("result", result);
		
		assertionConsumer.accept(assertion, execIndex);
		
		agent.shutdown();
		echo.it(agent.getDeployment());
	}
		
	public AutonomousAgent createAgent(Integer scenario, int _execIndex,
			List<String> initialCtxs,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding, 
			Consumer<Map<String, Integer>> weightMapBuilding, 
			ResponseEvaluation evaluation) {
		
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
