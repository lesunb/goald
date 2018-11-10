package goald.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import goald.design.model.ArchitectureUnit;
import goald.design.model.Component;
import goald.design.model.ContextCondition;
import goald.design.model.Decomposition;
import goald.design.model.Decomposition.Type;
import goald.design.model.Goal;
import goald.design.model.GoalModel;
import goald.design.model.Interf;
import goald.design.model.Node;
import goald.design.model.SubDecomposition;

public class ToGoalModel {

	private Map<String, List<Component>> componentsMap = new HashMap<>();
	private Map<String, Node> nodeMap = new HashMap<>();
	private List<Goal> goals = new ArrayList<>();
	
	private ToGoalModel() {
			
	}

	public static ToGoalModel instance() {
		return new ToGoalModel();
	}

	public GoalModel convert(List<ArchitectureUnit> units) {

		units.forEach( archUnit -> {
			if(archUnit instanceof Component ) {
				// group ORs Decomposition
				Component component = (Component) archUnit;
				getCompImplezList(component.getImplementz())
					.add(component);
			}else if(archUnit instanceof Interf ) {
				// each interface is a goal
				Interf interf = (Interf) archUnit;
				Goal goal = new Goal(interf.getName());
				nodeMap.put(interf.getName(), goal);
			}
		});
		
		//each non empty list of implements, with size greater the 1 is a or decomposition
		List<Node> nodes = new ArrayList<>();
		List<Decomposition> decompositions = new ArrayList<>();
		componentsMap.forEach((key, list) ->{
			if(list.size() == 1) {
				// interface with with only one implementation
				// AND refinement
				Component component = list.get(0);
				Decomposition deco = new Decomposition();
				deco.setType(Type.AND);
				decompositions.add(deco);
				
				component.getRequires().forEach(require -> {
					Node node = getLabeledNode(require);
					SubDecomposition e = new SubDecomposition(node, 
							new ContextCondition(component.getRequires().toString())); //TODO??
					
					deco.getSubDecompositions().add(e);
					nodes.add(node);
				});
			} else if(list.size() > 1) {
				// interface with with multiple implementations
				// OR refinement
				Decomposition deco = new Decomposition();
				deco.setType(Type.OR);
				decompositions.add(deco);
				list.forEach(component -> {
					Node node = componentToNode(component);
					SubDecomposition e = new SubDecomposition(node, 
							new ContextCondition(component.getRequires().toString())); //TODO??
					// TODO e.setInto(into);
					node.setDecomposition(deco);
					deco.getSubDecompositions().add(e);
					
					nodes.add(node);
				});
				
			}
		});
		System.out.println(nodes);
		System.out.println(decompositions);
		System.out.println(componentsMap);
		
		return null;
	}
	
	private Node getLabeledNode(String label) {
		return nodeMap.get(label);
	}
	
	private Node componentToNode(Component component) {
		Goal goal = new Goal(component.getName());
		return goal;
	}
	
	private List<Component> getCompImplezList(String implementz) {
		if(componentsMap.get(implementz) == null ) {
			componentsMap.put(implementz, new ArrayList<>());
		}
		return componentsMap.get(implementz);
		
	}
}
