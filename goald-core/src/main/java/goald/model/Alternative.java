package goald.model;

import java.util.ArrayList;
import java.util.List;

public class Alternative {
	
	private VE parentDame;
	
	private List<Goal> dependencyGoals;
	
	private boolean resolved = false;
	
	private List<VE> listDepDame;
	
	private List<ContextCondition> ctxReq;
	
	private Bundle impl;
	
	private Integer quality;

	public VE getParentDame() {
		return parentDame;
	}

	public void setParentDame(VE parentDame) {
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

	public Boolean getResolved() {
		return resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public List<VE> getListDepDame() {
		if(listDepDame == null) {
			listDepDame = new ArrayList<>();
		}
		return listDepDame;
	}

	public void setListDepDame(List<VE> listDepDame) {
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

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	@Override
	public String toString() {
		return "Alternative [impl=" + impl + ", dependencyGoals=" + dependencyGoals  + ", ctxReq=" + ctxReq
				+ ", resolved=" + resolved + ", listDepDame=" + listDepDame
				+ ", ctxSatisfied=" + ", impl=" + impl + ", quality=" + quality + "]";
	}
	
}
