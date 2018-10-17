package goald.design;

import java.util.ArrayList;
import java.util.List;

import goald.design.model.Node;
import goald.design.model.SubDecomposition;

public class DesignUtils {

	public static List<Node> getLeaves(Node node){
		List<Node> list = new ArrayList<>();
		if(node.getDecomposition() == null) {
			list.add(node);
		}else {
			for(SubDecomposition subDec : node.getDecomposition().getSubDecompositions()) {
				list.addAll(getLeaves(subDec.getInto()));
			}
		}
		return list;
	}
}
