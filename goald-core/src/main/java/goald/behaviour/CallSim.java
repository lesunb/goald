package goald.behaviour;

import goald.model.Alternative;

public class CallSim {

	Profile profile;
	
	RandomFailureTrower randomFailureTrower;
	
	public CallSim(Profile profile, RandomFailureTrower randomFailureTrower) {
		this.profile = profile;
		this.randomFailureTrower = randomFailureTrower;
	}
	
	public Float simulateCall(Alternative alternative) throws CallFailure {
		Float probl = profile.getForBundle(alternative.getImpl().getIdentification());
		randomFailureTrower.call(probl, alternative.getImpl().identification);
		return null;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
