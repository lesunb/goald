package goald.behaviour;

import java.util.List;

import goald.model.GoalDManager;
import goald.model.Alternative;
import goald.model.VE;

public class BehaviourSim {

	GoalDManager agent;
	CallSim bundleSim;
	
	public BehaviourSim(GoalDManager agent, Profile profile ) {
		this(agent, new CallSim(profile, new RandomFailureTrowerImp()));
	}
	
	public BehaviourSim(GoalDManager agent, CallSim bundleSim) {
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

	public Object call(VE dame, List<String> contexts) throws CallFailure {
		Alternative alternative = dame.getChosenAlt();
		
		if(alternative.getDependencyGoals().isEmpty()) {
			bundleSim.simulateCall(alternative);
		}
		
		return null;
	}
}
