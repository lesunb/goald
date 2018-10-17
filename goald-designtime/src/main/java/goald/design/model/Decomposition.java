package goald.design.model;

import java.util.ArrayList;
import java.util.List;

public class Decomposition {

	public static enum Type {
		AND,
		OR
	}

	private Type type;
	
	private Node from;
	
	private List<SubDecomposition> subDecompositions;
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Node getFrom() {
		return from;
	}

	public void setFrom(Node from) {
		this.from = from;
	}

	public List<SubDecomposition> getSubDecompositions() {
		if(subDecompositions == null) {
			this.subDecompositions = new ArrayList<>();
		}
		return subDecompositions;
	}

	public void setSubDecompositions(List<SubDecomposition> subDecompositions) {
		this.subDecompositions = subDecompositions;
	}

}
