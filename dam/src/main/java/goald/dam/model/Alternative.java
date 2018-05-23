package goald.dam.model;

import java.util.ArrayList;
import java.util.List;

public class Alternative {
	private Dame parentDame;
	
	private List<Goal> dependencyGoals;
	
	private Boolean ctxSatisfied;
	
	private Boolean resolved;
	
	private List<Dame> listDepDame;
	
	private List<ContextCondition> ctxReq;
	
	private Bundle impl;

	public Dame getParentDame() {
		return parentDame;
	}

	public void setParentDame(Dame parentDame) {
		this.parentDame = parentDame;
	}

	public List<Goal> getDependencyGoals() {
		if(dependencyGoals == null) {
			this.dependencyGoals = new ArrayList<>();
		}
		return dependencyGoals;
	}

	public void setDependencyGoals(List<Goal> dependencyGoals) {
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
		if(listDepDame == null) {
			listDepDame = new ArrayList<>();
		}
		return listDepDame;
	}

	public void setListDepDame(List<Dame> listDepDame) {
		this.listDepDame = listDepDame;
	}

	public List<ContextCondition> getCtxReq() {
		if(ctxReq == null) {
			ctxReq = new ArrayList<>();
		}
		return ctxReq;
	}

	public void setCtxReq(List<ContextCondition> ctxReq) {
		this.ctxReq = ctxReq;
	}

	public Bundle getImpl() {
		return impl;
	}

	public void setImpl(Bundle impl) {
		this.impl = impl;
	}
	
}
