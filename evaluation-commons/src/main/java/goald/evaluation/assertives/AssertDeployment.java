package goald.evaluation.assertives;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import goald.evaluation.assertives.Assertion.AssertionResult;
import goald.model.CtxEvaluator;
import goald.model.Deployment;
import goald.model.Deployment.Status;
import goald.model.util.ContextConditionUtils;

public class AssertDeployment {
	
	protected CtxEvaluator ctxEvaluator;
	
	protected List<Assertive> assertives;
	
	protected List<Assertion> assertion;
	
	
	public Assertion assertObservation(Observation obs) {
		Assertion ass = assertives.stream()
				//assertives that apply
				.filter((assertive)-> check(obs.ctx, assertive.premise))
			.map(assertIt(obs, obs.ctx.toString()))
			.collect(Assertion::new, Assertion::accept, Assertion::combine);
		return ass;
	}

	private Function<Assertive, Assertion> assertIt(Observation obs, String ...infos) {
		
		return (assertive)-> {
			boolean hasChanged = hasChanged(obs.initialDeployment, obs.finalDeployment);
				
			boolean passBeforeChange = check(obs.initialDeployment, assertive.conclusion);			
			boolean passAfterChange = check(obs.finalDeployment, assertive.conclusion);
			
			Assertion.AssertionResult analysisResult = null;
			if(hasChanged) {
				//true or false positive
				if(!passBeforeChange) {
					//fixing deployment!
					analysisResult = AssertionResult.TRUE_POSITIVE;
				} else {
					System.out.println("seams like a false positive for " + obs);
					analysisResult = AssertionResult.FALSE_POSITIVE;
				}
			}else {
				//true or false negative
				if(!passBeforeChange) {
					System.out.println("false negative for " + obs);
					analysisResult = AssertionResult.FALSE_NEGATIVE;
				} else {
					//Alright, noting to change 
					analysisResult = AssertionResult.TRUE_NEGATIVE;
				}
			}
			return new Assertion(analysisResult, passAfterChange, 
					assertive.toString(), infos.toString());
		};
	}

	private boolean hasChanged(Deployment initialDeployment, Deployment endDeployment) {
		return !initialDeployment.equals(endDeployment);
	}
	
	public static boolean check(CtxEvaluator ctxs, Propositions promise) {
		for(String notCtx: promise.hasNot) {
			if(ctxs.check(ContextConditionUtils.conditions(notCtx))) {
				return false;
			}
		}
		
		return ctxs.check(ContextConditionUtils.conditions(promise.has));
	}

	static boolean check(Deployment deployment, Propositions promise) {
		List<String> activeBundles = deployment
				.getAll(Status.ACTIVE).stream()
				.map(bundle -> bundle.identification)
		.collect(Collectors.toList());
		
		for(String notCtx: promise.hasNot) {
			if(activeBundles.indexOf(notCtx) >=0) {
				return false;
			}
		}
		for(String ctx: promise.has) {
			if(activeBundles.indexOf(ctx) < 0) {
				return false;
			}
		}
		return true;
	}

	public Assertion assertChange(CtxEvaluator ctx, Deployment initialDeployment, Deployment finalDeployment) {
		return assertObservation(new Observation(ctx, initialDeployment, finalDeployment));
	}
	 
}
