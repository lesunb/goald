package goald.dam.model;

import java.util.List;

public class Alternative {
	private Dame parentDame;
	
	private List<String> dependencyGoals;
	
	private Boolean ctxSatisfied;
	
	private Boolean resolved;
	
	private List<Dame> listDepDame;

	public Dame getParentDame() {
		return parentDame;
	}

	public void setParentDame(Dame parentDame) {
		this.parentDame = parentDame;
	}

	public List<String> getDependencyGoals() {
		return dependencyGoals;
	}

	public void setDependencyGoals(List<String> dependencyGoals) {
		this.dependencyGoals = dependencyGoals;
	}

	public Boolean getCtxSatisfied() {
		return ctxSatisfied;
	}

	public void setCtxSatisfied(Boolean ctxSatisfied) {
		this.ctxSatisfied = ctxSatisfied;
	}

	public Boolean getResolved() {
		return resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public List<Dame> getListDepDame() {
		return listDepDame;
	}

	public void setListDepDame(List<Dame> listDepDame) {
		this.listDepDame = listDepDame;
	}
	
}
