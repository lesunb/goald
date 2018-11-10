package goald;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import goald.conversion.ToComponents;
import goald.conversion.ToGoalModel;
import goald.design.model.ArchitectureUnit;
import goald.design.model.GoalModel;

public class ComponentsToGoalModelTest {

	GoalModel gooalModelA;
	GoalModel gooalModelB;

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

		 gooalModelB = GoalModelBuilder.create()
		 .root("G1")
		 .decomposition(
			 DecompositionsBuilder.create()
			 .or()
			 .intoTask("P1", "C1")
			 .intoTask("P2", "C2")
			 .build())
		.node("P1")
		.node("P2")
		.build();
	}

	@Test
	public void testConvertAndDecomposition() {
		List<ArchitectureUnit> components = ToComponents.instance().convert(gooalModelA);
		//components
		GoalModel generatedGoalModel = ToGoalModel.instance().convert(components);
		assertEquals("G4", generatedGoalModel.root.getIdentication());
		
	}
	
//	@Test
//	public void testConvertOrDecomposition() {
//		List<ArchitectureUnit> arch = ToComponents.instance().convert(gooalModelB);
//		assertEquals(3, arch.size());
//		assertEquals("interface G1 { }", arch.get(0).toString());
//		assertEquals("component P1Impl {	provides G1;}", arch.get(1).toString());
//		assertEquals("component P2Impl {	provides G1;}", arch.get(2).toString());
//	}

	// @Test
	// public void testModelWithOrBuilt() {
	// Assert.assertNull(rootOrGoal);
	// }
}
