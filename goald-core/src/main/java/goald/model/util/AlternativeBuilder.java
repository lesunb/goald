package goald.model.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import goald.model.Alternative;
import goald.model.Bundle;
import goald.model.ContextCondition;
import goald.model.Dependency;
import goald.model.Goal;
import goald.model.VE;

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
		this.alternative.setDependencies(impl.getDepends());
		this.alternative.setCtxReq(impl.getConditions());
		this.alternative.setImpl(impl);
		return this;
	}

	public AlternativeBuilder forDame(VE dame) {
		this.alternative.setParentDame(dame);
		return this;
	}

	public AlternativeBuilder withDependencies(List<Goal> dependencyGoals) {
		Stream<Dependency> dependencies = dependencyGoals.stream()
				.map( (goal -> new Dependency(goal.getIdentication())));
		this.alternative.setDependencies(dependencies.collect(Collectors.toList()));
		return this;
	}
}
