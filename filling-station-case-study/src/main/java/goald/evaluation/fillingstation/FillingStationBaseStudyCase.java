package goald.evaluation.fillingstation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.AutonomousAgent;
import goald.eval.exec.Evaluation;
import goald.exputil.EchoService;
import goald.model.ContextChange;
import goald.model.DeploymentPlan;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.DameRespository;
import goald.repository.IRepository;

public abstract class FillingStationBaseStudyCase implements IExperimentsExecutor {

	@Inject
	Logger log;

	@Inject
	EchoService echo;
	
	DameRespository repo;
	
	public CtxEvaluatorBuilder initialCtx;
	public GoalsChangeRequestBuilder goalsChangeBuilder;
	
	public abstract List<Evaluation> caseStudy();
	
	protected abstract IRepository getRepo();

	@Override
	public List<Evaluation> exec() {
		//setup environment
		repo = new DameRespository(getRepo());
		//execute deployment planning for case study
		return caseStudy();
	}
	
	public void scenarioRepetitions(int numOfRepetitions, Integer scenario, Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<List<ContextChange>> changesBuilding, 
			Collection<Evaluation> evaluations, Evaluation baseEvaluation) {
		
		for(int execIndex =1; execIndex<=numOfRepetitions; execIndex++){
			Evaluation evaluation = baseEvaluation.blankCopy();
			// eval.setFactors(factors);
			scenario(scenario, execIndex, ctxBuilding,
					weightMapBuilding, goalsChangeBuilding,
					changesBuilding, evaluation);
			evaluations.add(evaluation);
		}
	}


	public void scenario(Integer scenario, int execIndex, Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<Map<String, Integer>> weightMapBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding,
			Consumer<List<ContextChange>> changesBuilding, 
			Evaluation evaluation) {
	
		log.info("Executing scenario s{}, repetition {}", scenario, execIndex); 
		//run execution
		
		// agent contexts and goals
		AutonomousAgent agent = createAgent(scenario, execIndex, ctxBuilding, 
				goalsChangeBuilding, weightMapBuilding, evaluation);
		
		// contexts changes
		List<ContextChange> changes = new ArrayList<>();
		changesBuilding.accept(changes);
		evaluation.getFactors().put("scenario", scenario);

		evaluation.split(execIndex, "initing_agent");
		// start the agent. It will deploy for the initial goals
		agent.init(repo);
				
		// context changes
		try {
			evaluation.split(execIndex, "start_addapting");
			for(ContextChange change:changes) {
				agent.handleContextChange(change);
				evaluation.split(execIndex, "handling_change");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
				
		echo.it(agent.getDeployment());
	}
		
	public AutonomousAgent createAgent(Integer scenario, int _execIndex, Consumer<CtxEvaluatorBuilder> ctxBuilding,
			Consumer<GoalsChangeRequestBuilder> goalsChangeBuilding, 
			Consumer<Map<String, Integer>> weightMapBuilding, 
			Evaluation evaluation) {
		
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
				evaluation.split(execIndex, "changing_goals");
			}
			
			@Override
			public void damUpdated() {
				evaluation.split(execIndex, "dam_updated");
			}
			
			@Override
			public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
				echo.it(adaptPlan);
				evaluation.split(execIndex, "deployment_change_planned");
			}
			
			@Override
			public void deploymentChangeExecuted() {
				evaluation.split(execIndex, "deployment_change_excuted");

			}
		};
	}
}
