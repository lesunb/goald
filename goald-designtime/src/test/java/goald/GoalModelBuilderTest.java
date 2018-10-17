package goald;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import goald.design.DesignUtils;
import goald.design.model.GoalModel;
import goald.design.model.Node;

public class GoalModelBuilderTest {

	GoalModel gooalModelA;

	@Before
	public void setUp() throws Exception {
		this.gooalModelA = GoalModelBuilder.create()
				.root("G4")
				.decomposition(
					DecompositionsBuilder
						.create()
						.and()
						.intoTask("P12")
						.intoTask("P13")
						.build())
				.node("P12")
				.node("P13")
				.build();

		// Goal rootOrGoal = GoalModelBuilder.create()
		// .root()
		// .identification("G1")
		// .decomposition(
		// DecopositionBuilder.create()
		// .or()
		// .intoTask("P1", "C1")
		// .intoTask("P2", "C2")
		// .build())
		// .build();

	}

	@Test
	public void testModelWithAndBuilt() {
		Assert.assertNotNull(gooalModelA);
		assertEquals("G4", gooalModelA.root.getIdentication());
		assertEquals(3, gooalModelA.size);
	}

	 @Test
	 public void testTreeBuiltByCountingLeaves() {
		 List<Node> nodes = DesignUtils.getLeaves(this.gooalModelA.root);
		 assertEquals(2, nodes.size());
	 }
}
