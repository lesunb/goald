package goald.conversion;

import java.util.ArrayList;
import java.util.List;

import goald.design.model.ArchitectureUnit;
import goald.design.model.Component;
import goald.design.model.ContextCondition;
import goald.design.model.Decomposition;
import goald.design.model.GoalModel;
import goald.design.model.Interf;
import goald.design.model.Node;
import goald.design.model.SubDecomposition;

public class ToComponents {

	private List<ArchitectureUnit> created = new ArrayList<>();

	private ToComponents() {
		this.created = new ArrayList<>();
	}

	public static ToComponents instance() {
		return new ToComponents();
	}

	public List<ArchitectureUnit> convert(GoalModel model) {
		Interf interf = createInterface(model.root);
		created.add(interf);
		applyPartners(model.root, interf, null);
		return created;
	}

	private void applyPartners(Node node, Interf interf, ContextCondition contextCondition) {
		if (node.getDecomposition() != null) {
			Decomposition decomposition = node.getDecomposition();
			if (decomposition.getType() == Decomposition.Type.AND) {
				List<String> requires = new ArrayList<>();
				Component comp = createComponent(node, interf, requires, contextCondition);
				created.add(comp);
				for (SubDecomposition subDec : decomposition.getSubDecompositions()) {
					// recursive create each
					Interf subInterf = new Interf(subDec.getInto().getIdentication());
					created.add(subInterf);
					applyPartners(subDec.getInto(), subInterf, subDec.getContextCondition());
					// require each
					requires.add(subDec.getIntoIdentification());
				}
			} else if (decomposition.getType() == Decomposition.Type.OR) {
				for (SubDecomposition subDec : decomposition.getSubDecompositions()) {
					applyPartners(subDec.getInto(), interf, contextCondition);
				}
			}
		} else {
			Component comp = createComponent(node, interf, null, null);
			created.add(comp);
		}
	}

	private Interf createInterface(Node node) {
		return new Interf(node.getIdentication());
	}

	private Component createComponent(Node node, Interf interf, List<String> requires, ContextCondition contextCondition) {
		return new Component(node.getIdentication() + "Impl", interf.getName(), requires, contextCondition);
	}

}
