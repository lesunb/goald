package goald.model;

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
	
	Type type;
	
	ContextCondition condition;
	
	public DependencyModifier(Type type, ContextCondition condition){
		this.type = type;
		this.condition = condition;
	}
	
	public DependencyModifier(Type type){
		this.type = type;
	}

	@Override
	public String toString() {
		if(this.type.multy == TypeMulti.unary) {
			return type.toString();
		}else {
			return type + "" + condition;
		}
	}
	
	
}
