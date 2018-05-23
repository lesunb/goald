package goald.dam.model;

import java.util.List;

public class Agent {

	private List<ContextCondition> actualCtx;
	
	private Dame rootDame;

	public List<ContextCondition> getActualCtx() {
		return actualCtx;
	}

	public void setActualCtx(List<ContextCondition> actualCtx) {
		this.actualCtx = actualCtx;
	}

	public Dame getRootDame() {
		return rootDame;
	}

	public void setRootDame(Dame rootDame) {
		this.rootDame = rootDame;
	}
	
}
