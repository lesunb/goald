package goalp.evaluation.model;

public class ExecSpecBuilder {
	
	protected ExecSpec execSpec;
	
	protected ExecSpecBuilder(){
		this.execSpec = new ExecSpec();
	}
	
	public static ExecSpecBuilder create(){
		return new ExecSpecBuilder();
	}
	
	public ExecSpec build(){
		ExecSpec built = this.execSpec;
		this.execSpec = null;
		return built;
	}

	public ExecSpecBuilder put(String key, Object value) {
		this.execSpec.put(key, value);
		return this;
	}


}
