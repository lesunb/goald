package goald.dam.model.util;

import goald.dam.model.Alternative;

public class AlternativeBuilder {
	
	protected Alternative alternative;
	
	protected AlternativeBuilder(){
		this.alternative = new Alternative();
	}
	
	public static AlternativeBuilder create(){
		return new AlternativeBuilder();
	}
	
	public Alternative build(){
		Alternative built = this.alternative;
		this.alternative = null;
		return built;
	}

	public AlternativeBuilder addCtxReq(String ctx) {
		this.alternative.getCtxReq().add(ctx);
		return this;
	}
	
}
