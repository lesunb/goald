package goald; 

import java.util.HashMap;
import java.util.Map;

import goald.execution.DeploymentExecutor;
import goald.model.Agent;
import goald.model.ContextChange;
import goald.model.Deployment;
import goald.model.DeploymentPlan;
import goald.model.GoalsChangeRequest;
import goald.model.util.AgentBuilder;
import goald.model.util.CtxEvaluatorBuilder;
import goald.model.util.GoalsChangeRequestBuilder;
import goald.planning.ContextChangeHandler;
import goald.planning.DameRespository;
import goald.planning.DeploymentPlanner;
import goald.planning.GoalsChangeHandler;
import goald.repository.IRepository;

public abstract class AutonomousAgent {
	
	private GoalsChangeHandler gcHandler;
	private ContextChangeHandler ccHandler;
	
	//planning
	private DeploymentPlanner deploymentPlanner;
	
	//execution
	private DeploymentExecutor executor;
	
	// knowledge
	private Agent agent;
	
	protected int version = 0;
	
	public abstract void setup(CtxEvaluatorBuilder ctxEvaluatorBilder, GoalsChangeRequestBuilder goalsChangeBuilder, Map<String, Integer> weightMap);
	
	public void handleContextChange(ContextChange change) {
		this.version++;
		DeploymentPlan adaptPlan;
		ccHandler.handle(change);
		damUpdated();
		adaptPlan = deploymentPlanner.createPlan();
		deploymentChangePlanCreated(adaptPlan);
		executor.execute(adaptPlan);
		deploymentChangeExecuted();
	}
	
	public void changingGoals() {}
	
	public void damUpdated() {}
	
	public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {}
	
	public void deploymentChangeExecuted() {}
	
	public void init(DameRespository repo) {
		CtxEvaluatorBuilder ctxEvaluatorBilder = CtxEvaluatorBuilder.create();
		
		GoalsChangeRequestBuilder goalsChangeBuilder = GoalsChangeRequestBuilder.create();
		
		Map<String, Integer> weights = new HashMap<String, Integer>();
		setup(ctxEvaluatorBilder, goalsChangeBuilder, weights);
		
		AgentBuilder agentBuilder =  AgentBuilder.create()
			.withContext(ctxEvaluatorBilder.build())
			.withQualityWeight(weights);

		agent = agentBuilder.build();
		GoalsChangeRequest goalsChangeRequest = goalsChangeBuilder.build();
		
		// construct the goald systems
		gcHandler = new GoalsChangeHandler(repo, agent);
		deploymentPlanner = new DeploymentPlanner(repo, agent);
		executor = new DeploymentExecutor(agent);
		ccHandler = new ContextChangeHandler(repo, agent);
		
		changingGoals();
		gcHandler.handle(goalsChangeRequest);
		damUpdated();
		DeploymentPlan initialPlan = deploymentPlanner.createPlan();
		deploymentChangePlanCreated(initialPlan);
		executor.execute(initialPlan);
		deploymentChangeExecuted();
		// exec the experiment
	}
	
	public void init(IRepository _repo) {
		init(new DameRespository(_repo));
	}
	
	public Deployment getDeployment() {
		return agent.getDeployment();
	}
	
	public Agent getAgent() {
		return this.agent;
	}

	@Override
	public String toString() {
		return "AutonomousAgent [agent=" + agent + ", gcHandler=" + gcHandler + ", ccHandler=" + ccHandler + ", deploymentPlanner="
				+ deploymentPlanner + ", executor=" + executor + "]";
	}

}
