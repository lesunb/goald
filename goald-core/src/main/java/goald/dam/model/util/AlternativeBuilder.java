package goald.dam.model.util;

import java.util.List;

import goald.dam.model.Alternative;
import goald.dam.model.Bundle;
import goald.dam.model.ContextCondition;
import goald.dam.model.Dame;
import goald.dam.model.Goal;

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

	public AlternativeBuilder from(Bundle def, Bundle impl) {
		this.alternative.setDependencyGoals(impl.getDepends());
		this.alternative.setCtxReq(impl.getConditions());
		this.alternative.setImpl(impl);
		return this;
	}

	public AlternativeBuilder forDame(Dame dame) {
		this.alternative.setParentDame(dame);
		return this;
	}

	public AlternativeBuilder withDependencies(List<Goal> dependencyGoals) {
		this.alternative.setDependencyGoals(dependencyGoals);
		return this;
	}
}
