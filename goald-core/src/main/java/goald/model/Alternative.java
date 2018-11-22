package goald.model;

import java.util.ArrayList;
import java.util.List;

public class Alternative {
	
	private VE parentDame;
	
	private List<Dependency> dependencies;
	
	private boolean resolved = false;
	
	private List<VE> listDepVE;
	
	private List<ContextCondition> ctxReq;
	
	private Bundle impl;
	
	private Integer quality;

	public VE getParentVE() {
		return parentDame;
	}

	public void setParentDame(VE parentDame) {
		this.parentDame = parentDame;
	}

	public List<Dependency> getDependencies() {
		if(dependencies == null) {
			this.dependencies = new ArrayList<>();
		}
		return dependencies;
	}

	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	public Boolean getResolved() {
		return resolved;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public List<VE> getListDepVE() {
		if(listDepVE == null) {
			listDepVE = new ArrayList<>();
		}
		return listDepVE;
	}

	public void setListDepVE(List<VE> listDepVE) {
		this.listDepVE = listDepVE;
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
		return "Alternative [impl=" + impl + ", dependencies=" + dependencies  + ", ctxReq=" + ctxReq
				+ ", resolved=" + resolved + ", listDepVE=" + listDepVE
				+ ", ctxSatisfied=" + ", impl=" + impl + ", quality=" + quality + "]";
	}
	
}
