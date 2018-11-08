package goald.profile;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ProfileService {

	Profile profile;
	
	public String getValue(String serviceName) {
		List<Variable> variables = profile.getRatioVariableMap().get(serviceName);
		
		Double randomNumber = Math.random();		
		Float rule = 0.0f;
		
		for(Variable variable: variables) {
			rule += variable.ratio;
			if(randomNumber < rule ) {
				return variable.value;
			}
		}
		
		throw new IllegalStateException("service not available");
	}
	
	public boolean checkSuccess(String serviceName) {
		List<Variable> variables = profile.getRatioVariableMap().get(serviceName);
		
		Double randomNumber = Math.random();		
		
		if(variables.get(0).ratio > randomNumber) {
			return false;
		}
		return true;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
