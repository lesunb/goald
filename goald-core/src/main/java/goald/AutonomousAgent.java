package goald; 

import java.util.HashMap;
import java.util.Map;

import goald.analysis.DeploymentChecker;
import goald.execution.DeploymentExecutor;
import goald.model.GoalDManager;
import goald.model.Change;
import goald.model.ContextChange;
import goald.model.Deployment;
import goald.model.DeploymentPlan;
import goald.model.GoalsChangeRequest;
import goald.model.util.AgentBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.ContextChangeHandler;
import goald.planning.VERespository;
import goald.planning.DeploymentPlanner;
import goald.planning.GoalsChangeHandler;
import goald.repository.IRepository;

public abstract class AutonomousAgent {
	
	private GoalsChangeHandler gcHandler;
	private ContextChangeHandler ccHandler;
	
	//analysis
	protected DeploymentChecker deploymenChecker;
	
	//planning
	protected DeploymentPlanner deploymentPlanner;
	
	//execution
	protected DeploymentExecutor executor;
	
	// knowledge
	private GoalDManager agent;
	
	protected int version = 0;
	
	
	public abstract void setup(CtxEvaluatorBuilder ctxEvaluatorBilder, GoalsChangeRequestBuilder goalsChangeBuilder, Map<String, Integer> weightMap);
	
	public void handleContextChange(ContextChange change) {
		this.version++;
		onChange(change);
		this.agent.getActualCtx().update(change);
		Boolean checkResult = deploymenChecker.check(change);
		if(!checkResult) { onFailure(change); }
		
		beforePlanningForContextChange(change);
		boolean planningResult = ccHandler.handle(change);
		onUpdate(change);
		afterPlanningForContextChange(change, planningResult);
		damUpdated();
		DeploymentPlan adaptPlan;
		adaptPlan = deploymentPlanner.createPlan();
		onDeploymentChangePlanned(adaptPlan);
		executor.execute(adaptPlan);
		onDeploymentChangeExecuted(change, adaptPlan);
	}
	
	public void init(VERespository repo) {
		CtxEvaluatorBuilder ctxEvaluatorBilder = CtxEvaluatorBuilder.create();
		
		GoalsChangeRequestBuilder goalsChangeBuilder = GoalsChangeRequestBuilder.create();
		
		Map<String, Integer> weights = new HashMap<String, Integer>();
		setup(ctxEvaluatorBilder, goalsChangeBuilder, weights);
		
		AgentBuilder agentBuilder =  AgentBuilder.create()
			.withContext(ctxEvaluatorBilder.build())
			.withQualityWeight(weights);

		agent = agentBuilder.build();
		onStartup();
		GoalsChangeRequest goalsChangeRequest = goalsChangeBuilder.build();
		
		// construct the goald systems
		gcHandler = new GoalsChangeHandler(repo, agent);
		deploymentPlanner = new DeploymentPlanner(repo, agent);
		executor = new DeploymentExecutor(agent);
		ccHandler = new ContextChangeHandler(repo, agent);
		deploymenChecker = new DeploymentChecker(agent);
		
		onChange(goalsChangeRequest);
		beforeChangeGoals(goalsChangeRequest);
		gcHandler.handle(goalsChangeRequest);
		onUpdate(goalsChangeRequest);
		damUpdated();
		DeploymentPlan initialPlan = deploymentPlanner.createPlan();
		onDeploymentChangePlanned(initialPlan);
		onExecute(goalsChangeRequest, initialPlan);
		executor.execute(initialPlan);
		
		onDeploymentChangeExecuted(goalsChangeRequest, initialPlan);
		// exec the experiment
	}
		
	public void onFailure(Change change) {}
	
	public void onChange(Change change) {}
	
	public void onUpdate(Change change) {}
	
	public void onExecute(Change change, DeploymentPlan initialPlan) {}
	
	public void onStartup() {}
	
	public void onShutdown() {}
	
	public void beforeChangeGoals(GoalsChangeRequest goalsChangeRequest) {}

	public void beforePlanningForContextChange(ContextChange change) {}

	public void afterPlanningForContextChange(ContextChange change, boolean result) {}
	
	public void damUpdated() {}
	
	public void onDeploymentChangePlanned(DeploymentPlan adaptPlan) {}
	
	public void onDeploymentChangeExecuted(Change change, DeploymentPlan adaptPlan) {}
	

	public void init(IRepository _repo) {
		init(new VERespository(_repo));
	}
	
	public Deployment getDeployment() {
		return agent.getDeployment();
	}
	
	public GoalDManager getAgent() {
		return this.agent;
	}
	
	public void shutdown() { 
		onShutdown();
	}

	@Override
	public String toString() {
		return "AutonomousAgent [agent=" + agent + ", gcHandler=" + gcHandler + ", ccHandler=" + ccHandler + ", deploymentPlanner="
				+ deploymentPlanner + ", executor=" + executor + "]";
	}
}
