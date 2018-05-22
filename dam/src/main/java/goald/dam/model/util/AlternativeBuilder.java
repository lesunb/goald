package goald.dam.model.util;

import goald.dam.model.Alternative;
import goald.dam.model.ContextCondition;

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

	public AlternativeBuilder addCtxReq(ContextCondition ctx) {
		this.alternative.getCtxReq().add(ctx);
		return this;
	}

	public AlternativeBuilder requiresCtx(String label) {
		return addCtxReq(new ContextCondition(label));
	}
	
	public AlternativeBuilder requiresCtx(String ...labels){
		for(String label: labels) {
			requiresCtx(label);	
		}
		return this;
	}
}
