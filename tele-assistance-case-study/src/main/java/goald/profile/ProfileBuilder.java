package goald.profile;

public class ProfileBuilder {
	
	private Profile built;
	
	private ProfileBuilder(){
		this.built = new Profile();
	}
	
	public static ProfileBuilder create(){
		return new ProfileBuilder();
	}
	
	public Profile build(){
		Profile built = this.built;
		this.built = null;
		return built;
	}
	
	public ProfileBuilder withFailureRate(String serviceName, Float failureRate) {
		Variable _var1 = new Variable( serviceName, failureRate);
		this.built.getVariableList(serviceName)
		.add(_var1);
		
		return this;
	}

	public ProfileBuilder forVariable(String serviceName, Object ...varArgs) {
		for(int i=0;i<varArgs.length; i+=2) {
			Variable _var1 = new Variable( (String) varArgs[i], (float) varArgs[i+1]);
			this.built.getVariableList(serviceName)
			.add(_var1);
		}
		return this;
	}
	
	
}
