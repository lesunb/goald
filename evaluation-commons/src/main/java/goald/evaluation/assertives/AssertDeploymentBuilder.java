package goald.evaluation.assertives;

import java.util.ArrayList;
import java.util.List;

import goald.model.CtxEvaluator;

public class AssertDeploymentBuilder {
	
	protected List<Assertive> assertives = new ArrayList<>();
	
	protected AssertDeployment built;
	
	protected AssertDeploymentBuilder(){
		this.built = new AssertDeployment();
	}
	
	public static AssertDeploymentBuilder create(){ 
		return new AssertDeploymentBuilder();
	}
	
	public AssertDeployment build(){
		return built;
	}

	public static Propositions promiseAux() {
		return new Propositions();
	}

	public AssertDeploymentBuilder withAssert(Propositions premise, Propositions conclusion) {
		if(built.assertives == null) {
			built.assertives = new ArrayList<>();
		}
		built.assertives.add(new Assertive(premise, conclusion));
		return this;
	}
}

