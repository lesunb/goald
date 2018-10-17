package goald;

import java.util.HashMap;
import java.util.Map;

import goald.design.model.Decomposition;
import goald.design.model.Goal;
import goald.design.model.GoalModel;
import goald.design.model.Node;
import goald.design.model.SubDecomposition;

public class GoalModelBuilder {

	private GoalModel model;
	
	private Node lastNode;
	
	private Map<String, SubDecomposition> subDecompositionsMap;
	
	private GoalModelBuilder() {
		this.model = new GoalModel();
	}
	
	public static GoalModelBuilder create() {
		return new GoalModelBuilder();
	}

	public GoalModelBuilder root(String identification) {
		model.root = new Goal(identification);
		this.model.size++;
		lastNode = model.root;
		return this;
	}
	
	public GoalModelBuilder node(String identification) {
		Node node = new Node(identification);
		lastNode = node;
		return this.node(node);
	}
	
	public GoalModelBuilder node(Node node) {
		this.model.size++;
		setNoteFromSubDecompositionsMap(node);
		return this;
	}
	
	public GoalModelBuilder decomposition(Decomposition decomposition) {
		lastNode.setDecomposition(decomposition);
		addToSubDecompositionsMap(decomposition);
		return this;
	}
	
	public void addToSubDecompositionsMap(Decomposition decomposition) {
		for(SubDecomposition subdecomposition: decomposition.getSubDecompositions()) {
			getSubDecompositionsMap().put(subdecomposition.getIntoIdentification(), subdecomposition);	
		}
	}
	
	public void setNoteFromSubDecompositionsMap(Node node) {
		SubDecomposition deco = getSubDecompositionsMap().get(node.getIdentication());
		if(deco == null) {
			throw new RuntimeException("adding node witout decomposition: " +node.getIdentication());
		}
		deco.setInto(node);
	}
	
	private Map<String, SubDecomposition> getSubDecompositionsMap() {
		if(this.subDecompositionsMap == null) {
			this.subDecompositionsMap = new HashMap<>();
		}
		return this.subDecompositionsMap;
	}
	
	public GoalModel build() {
		return this.model;
	}
	
}
