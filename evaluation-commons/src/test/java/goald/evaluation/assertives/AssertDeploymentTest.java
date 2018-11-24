package goald.evaluation.assertives;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.evaluation.assertives.Assertion.AssertionResult;
import goald.model.CtxEvaluator;
import goald.model.Deployment;
import goald.model.Deployment.Status;
import goald.model.util.CtxEvaluatorBuilder;

public class AssertDeploymentTest {

	Propositions premise = AssertDeploymentBuilder.promiseAux();
	Propositions conclusion = AssertDeploymentBuilder.promiseAux();

	@Before
	public void setup() {
		
	}
	
	
	@Test
	public void assertTrueNegative() {
		AssertDeployment assertDeployment = AssertDeploymentBuilder.create()
				.withAssert(
						premise
							.has("doctor-is-present")
							.hasnot("internet-connection").close(),
						conclusion
							.has("ChangeDose-impl")
							.close())
				.build();
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("doctor-is-present")
				.build();
		
		Deployment deploymentA = new Deployment();
		deploymentA.add(Status.ACTIVE, "ChangeDose-impl");

		Assertion result = assertDeployment
				.assertChange(ctx,
						deploymentA, deploymentA.clone());
		
		Assert.assertEquals(AssertionResult.TRUE_NEGATIVE, result.getResult());
		Assert.assertEquals(true, result.passAfterChange);
	}
	
	@Test
	public void assertFalseNegativeFinalInvalidDepl() {
		AssertDeployment assertDeployment = AssertDeploymentBuilder.create()
				.withAssert(
						premise
							.has("doctor-is-present")
							.hasnot("internet-connection").close(),
						conclusion
							.has("ChangeDose-impl")
							.close())
				.build();
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("doctor-is-present")
				.build();
		
		Deployment deployment = new Deployment();
		deployment.add(Status.ACTIVE, "ChangeDrug-impl"); //wrong
				
		Assertion result = assertDeployment
				.assertChange(ctx,
						deployment, deployment.clone());
		
		Assert.assertEquals(AssertionResult.FALSE_NEGATIVE, result.getResult());
		Assert.assertEquals(false, result.allPass);
	}
	
	
	@Test
	public void assertTruePositibe() {
		AssertDeployment assertDeployment = AssertDeploymentBuilder.create()
				.withAssert(
						premise
							.has("doctor-is-present")
							.hasnot("internet-connection").close(),
						conclusion
							.has("ChangeDrug-impl")
							.close())
				.build();
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("doctor-is-present")
				.build();
		
		Deployment deployment = new Deployment();
		deployment.add(Status.ACTIVE, "ChangeDose-impl"); // wrong
		
		Deployment deploymentB = new Deployment();
		deploymentB.add(Status.ACTIVE, "ChangeDrug-impl"); // then right
		
		Assertion result = assertDeployment
				.assertChange(ctx,
						deployment, deploymentB);
		
		Assert.assertEquals(AssertionResult.TRUE_POSITIVE, result.getResult());
		Assert.assertEquals(true, result.passAfterChange);
	}
	
	@Test
	public void assertTruePositiveFinalInvalidDepl() {		
		AssertDeployment assertDeployment = AssertDeploymentBuilder.create()
				.withAssert(
						premise
							.has("doctor-is-present")
							.hasnot("internet-connection").close(),
						conclusion
							.has("RemoteAnalyze-impl")
							.close())
				.build();
		
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("doctor-is-present")
				.build();
		
		Deployment deployment = new Deployment();
		deployment.add(Status.ACTIVE, "ChangeDose-impl"); // wrong
		
		Deployment deploymentB = new Deployment();
		deploymentB.add(Status.ACTIVE, "ChangeDrug-impl"); // wrong
		
		Assertion result = assertDeployment
				.assertChange(ctx,
						deployment, deploymentB);
		
		Assert.assertEquals(AssertionResult.TRUE_POSITIVE, result.getResult());
		Assert.assertEquals(false, result.passAfterChange);
	}

}
