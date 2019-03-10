package goald.model;

import java.util.List;

public class DependencyModifier {

	public enum TypeMulti {
		unary,
		binary
	}
	
	public enum Type {
		ONE(TypeMulti.unary),
		ANY(TypeMulti.unary), 
		COND(TypeMulti.binary);
		
		TypeMulti multy;
		Type(TypeMulti multy) {
			this.multy = multy;
		}
	};
	
	private Type type;
	
	private List<ContextCondition> conditions;
	
	private int groupId;

	public DependencyModifier(Type type, List<ContextCondition> conditions){
		this.type = type;
		this.conditions = conditions;
	}
	
	public DependencyModifier(Type type, int groupId){
		this.type = type;
		this.groupId = groupId;
	}
	
	public DependencyModifier(Type type){
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

	public  List<ContextCondition> getConditions() {
		return conditions;
	}

	@Override
	public String toString() {
		if(this.type.multy == TypeMulti.unary) {
			return type.toString();
		}else {
			return type + ":" + conditions;
		}
	}
	
	
}
