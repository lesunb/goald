package goald; 

import goald.dam.execution.DeploymentExecutor;
import goald.dam.model.Agent;
import goald.dam.model.ContextChange;
import goald.dam.model.Deployment;
import goald.dam.model.DeploymentPlan;
import goald.dam.model.GoalsChangeRequest;
import goald.dam.model.util.AgentBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;
import goald.dam.model.util.GoalsChangeRequestBuilder;
import goald.dam.planning.ContextChangeHandler;
import goald.dam.planning.DameRespository;
import goald.dam.planning.DeploymentPlanner;
import goald.dam.planning.GoalsChangeHandler;
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
	
	public abstract void setup(CtxEvaluatorBuilder ctxEvaluatorBilder, GoalsChangeRequestBuilder goalsChangeBuilder);
	
	public void handleContextChange(ContextChange change) {		
		DeploymentPlan adaptPlan;
		ccHandler.handle(change);
		damUpdated();
		adaptPlan = deploymentPlanner.createPlan();
		deploymentChangePlanCreated(adaptPlan);
		executor.execute(adaptPlan);
		deploymentChangeExecuted();
	}

	public void damUpdated() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void deploymentChangePlanCreated(DeploymentPlan adaptPlan) {
		// TODO Auto-generated method stub
		
	}
	
	public void deploymentChangeExecuted() {
		// TODO Auto-generated method stub
		
	}

	public void init(IRepository _repo) {
		DameRespository repo = new DameRespository(_repo);
		
		CtxEvaluatorBuilder ctxEvaluatorBilder = CtxEvaluatorBuilder.create();
		
		GoalsChangeRequestBuilder goalsChangeBuilder = GoalsChangeRequestBuilder.create();
		
		setup(ctxEvaluatorBilder, goalsChangeBuilder);
		
		AgentBuilder agentBuilder =  AgentBuilder.create()
			.withContext(ctxEvaluatorBilder.build());

		agent = agentBuilder.build();
		GoalsChangeRequest goalsChangeRequest = goalsChangeBuilder.build();
		
		// construct the goald systems
		gcHandler = new GoalsChangeHandler(repo, agent);
		deploymentPlanner = new DeploymentPlanner(repo, agent);
		executor = new DeploymentExecutor(agent);
		ccHandler = new ContextChangeHandler(repo, agent);

		gcHandler.handle(goalsChangeRequest);
		damUpdated();
		DeploymentPlan initialPlan = deploymentPlanner.createPlan();
		deploymentChangePlanCreated(initialPlan);
		executor.execute(initialPlan);
		deploymentChangeExecuted();
		// exec the experiment
	}
	
	public Deployment getDeployment() {
		return agent.getDeployment();
	}
	
	public Agent getAgent() {
		return this.agent;
	}

}
