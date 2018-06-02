package goalp.model;

import java.util.Arrays;
import java.util.Collection;

import goalp.model.resources.Display;

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

	public AgentBuilder addResource(Display display) {
		// TODO Auto-generated method stub
		return this;
	}

	public AgentBuilder addContext(String context) {
		this.agent.getContext().add(context);
		return this;
	}
	public AgentBuilder addContexts(Collection<String> contexts) {
		this.agent.getContext().addAll(contexts);
		return this;
	}

	public AgentBuilder addContexts(String... contexts) {
		this.agent.getContext().addAll(Arrays.asList(contexts));
		return this;
	}
}
