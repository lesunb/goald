package goald.dam.planning;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Alternative;
import goald.dam.model.util.AlternativeBuilder;
import goald.dam.model.util.CtxEvaluatorBuilder;

public class UpdateDamTest {
	
	DamUpdater updater;
	
	@Before
	public void setup() {
		updater = new DamUpdater(null, null);
	}
	

	@Test
	public void testResolveIfLeaf() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
		.with("C1")
		.build();
		
		Alternative alt = new Alternative();
		DamUpdater updater = new DamUpdater(null, null);
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertTrue(result);
	}

	
	@Test
	public void testResolveAltInANotValidContext() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("C1")
				.build();
		
		Alternative alt = AlternativeBuilder
				.create()
				.requiresCtx("C1", "C2")
				.build();
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertFalse(result);
	}
	
	@Test
	public void testResolveAltInAContextValid() {
		CtxEvaluator ctx = CtxEvaluatorBuilder.create()
				.with("C1", "C2", "C3")
				.build();
		
		Alternative alt = AlternativeBuilder
				.create()
				.requiresCtx("C1", "C2")
				.build();
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertTrue(result);
	}
	
	
}
