package goald.behaviour;

import java.util.List;

import goald.model.Agent;
import goald.model.Alternative;
import goald.model.Dame;

public class BehaviourSim {

	Agent agent;
	CallSim bundleSim;
	
	public BehaviourSim(Agent agent, Profile profile ) {
		this(agent, new CallSim(profile, new RandomFailureTrowerImp()));
	}
	
	public BehaviourSim(Agent agent, CallSim bundleSim) {
		this.agent = agent;
		this.bundleSim = bundleSim;
	}
	
	public Object call(List<String> contexts) {
		bundleSim.clear();
		CallResult result = new CallResult();
		try {
			call(agent.getRootDame(), contexts);
			result.setSuccess(true);
		}catch(CallFailure err) {
			result.setSuccess(false);
			result.setErrorCause(err.identification);
		}
		return result;	
	}

	public Object call(Dame dame, List<String> contexts) throws CallFailure {
		Alternative alternative = dame.getChosenAlt();
		
		if(alternative.getDependencyGoals().isEmpty()) {
			bundleSim.simulateCall(alternative);
		}
		
		return null;
	}
}
