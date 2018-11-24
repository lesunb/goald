package goald.evaluation.assertives;

import org.junit.Assert;
import org.junit.Test;

import goald.model.CtxEvaluator;
import goald.model.Deployment;
import goald.model.Deployment.Status;
import goald.model.util.CtxEvaluatorBuilder;

public class PremiseAssertTest {

	AssertDeployment assertDeployment;
	
	@Test
	public void assertCheckPassingPremiseWithHas() {
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("!pattient-is-ok", "doctor-is-present", "random")
				.build();
		
		Propositions premise = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(ctx, premise
					.has("!pattient-is-ok", "doctor-is-present")
					.hasnot("has-not-context")
					.close());
		Assert.assertTrue(result);
	}
	
	@Test
	public void assertCheckNotPassingPremiseWithHas() {
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("!pattient-is-ok", "doctor-is-present", "random")
				.build();
		
		Propositions premise = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(ctx, premise
					.has("!pattient-is-ok", "doctor-is-present", "has-not-context")
					.hasnot("has-not-context")
					.close());
		Assert.assertFalse(result);
	}
	
	@Test
	public void assertCheckPassingPremiseWithHasNot() {
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("!pattient-is-ok", "doctor-is-present", "random")
				.build();
		
		Propositions premise = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(ctx, premise
					.has("!pattient-is-ok")
					.hasnot("ctx-do-not-has")
					.close());
		Assert.assertTrue(result);
	}
	
	@Test
	public void assertCheckNotPassingPremiseWithHasNot() {
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("!pattient-is-ok", "doctor-is-present", "random")
				.build();
		
		Propositions premise = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(ctx, premise
					.hasnot("doctor-is-present")
					.close());
		Assert.assertFalse(result);
	}

	
	@Test
	public void assertCheckConclusionPassingWithHas() {
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("gps_capability")
				.with("!pattient-is-ok", "doctor-is-present", "random")
				.build();
		
		Propositions premise = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(ctx, premise
					.has("!pattient-is-ok", "doctor-is-present")
					.hasnot("has-not-context")
					.close());
		Assert.assertTrue(result);
	}
	
	@Test
	public void assertConclusionPremiseWithHasPassing() {
		
		
		Deployment deployment = new Deployment();
		deployment.add(Status.ACTIVE, "ChangeDose-impl");
		
		Propositions conclusion = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(deployment, conclusion
					.has("ChangeDose-impl")
					.hasnot("has-not-context")
					.close());
		Assert.assertTrue(result);
	}
	
	@Test
	public void assertConclusionPremiseWithHasNotNotPassing() {
		
		
		Deployment deployment = new Deployment();
		deployment.add(Status.ACTIVE, "ChangeDose-impl");
		
		Propositions conclusion = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(deployment, conclusion
					.hasnot("ChangeDose-impl")
					.close());
		Assert.assertFalse(result);
	}
	
	@Test
	public void assertConclusionPremiseWithHasNotPassing() {
		
		
		Deployment deployment = new Deployment();
		deployment.add(Status.ACTIVE, "ChangeDose-impl");
		
		Propositions conclusion = AssertDeploymentBuilder.promiseAux();
		
		boolean result = AssertDeployment
			.check(deployment, conclusion
					.has("has-not-context")
					.close());
		Assert.assertFalse(result);
	}
	
}
