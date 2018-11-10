package goald.behaviour;

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
	
	public ProfileBuilder withFailureRate(String identificationBundle, Float failureRate) {
		this.built.getRateFailureMap().put(identificationBundle, failureRate);
		return this;
	}
}
