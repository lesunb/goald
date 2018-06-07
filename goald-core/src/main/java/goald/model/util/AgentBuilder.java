package goald.model.util;

import java.util.Map;

import goald.model.Agent;
import goald.model.CtxEvaluator;

public class AgentBuilder {
	
	private Agent agent;
	
	private AgentBuilder(){
		this.agent = new Agent();
	}
	
	public static AgentBuilder create(){
		return new AgentBuilder();
	}
	
	public Agent build(){
		Agent built = this.agent;
		this.agent = null;
		return built;
	}
	
	public AgentBuilder withQualityWeight(String label, int value) {
		this.agent.setWeight(label, value);
		return this;
	}
	
	public AgentBuilder withQualityWeight(Map<String, Integer> weightMap) {
		this.agent.setWeightMap(weightMap);
		return this;
	}

	public AgentBuilder withContext(CtxEvaluator ctx) {
		this.agent.setActualCtx(ctx);
		return this;
	}
	

//	public AgentBuilder addContext(String context) {
//		this.agent.getContext().add(context);
//		return this;
//	}
//	public AgentBuilder addContexts(Collection<String> contexts) {
//		this.agent.getContext().addAll(contexts);
//		return this;
//	}
//
//	public AgentBuilder addContexts(String... contexts) {
//		this.agent.getContext().addAll(Arrays.asList(contexts));
//		return this;
//	}
}
