package goald.dam.planning;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.dam.model.Alternative;
import goald.dam.model.util.AlternativeBuilder;

public class UpdateDameTest {
	
	UpdateDame updater;
	
	@Before
	public void setup() {
		updater = new UpdateDame(null, null);
	}
	

	@Test
	public void testIfLeaf() {
		List<String> ctx = new ArrayList<>();
		ctx.add("c1");
		
		Alternative alt = new Alternative();
		UpdateDame updater = new UpdateDame(null, null);
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertTrue(result);
	}

	
	@Test
	public void testContextNotValid() {
		List<String> ctx = new ArrayList<>();
		ctx.add("c1");
		
		Alternative alt = AlternativeBuilder
				.create()
				.addCtxReq("c2")
				.build();
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertFalse(result);
	}
	
	@Test
	public void testContextValid() {
		List<String> ctx = new ArrayList<>();
		ctx.add("c1");
		ctx.add("c2");
		ctx.add("c3");
		
		
		Alternative alt = AlternativeBuilder
				.create()
				.addCtxReq("c1")
				.addCtxReq("c2")
				.build();
		
		boolean result = updater.resolveAlt(ctx, alt);
		
		Assert.assertTrue(result);
	}
}
