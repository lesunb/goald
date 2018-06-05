package goald.model;

import java.util.ArrayList;
import java.util.List;


public class Bundle {

	public String uuid;

	public String identification;

	protected List<Goal> defines;

	protected List<ContextCondition> conditions;

	protected List<Goal> provides;

	protected List<Goal> depends;
	
	protected List<QualityParameter> qualityParams;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public List<Goal> getDefines() {
		if (this.defines == null) {
			this.defines = new ArrayList<>();
		}
		return defines;
	}

	public void setDefines(List<Goal> defines) {
		this.defines = defines;
	}

	public List<ContextCondition> getConditions() {
		if (this.conditions == null) {
			this.conditions = new ArrayList<>();
		}
		return conditions;
	}

	public void setConditions(List<ContextCondition> conditions) {
		this.conditions = conditions;
	}

	public List<Goal> getProvides() {
		if (this.provides == null) {
			this.provides = new ArrayList<>();
		}
		return provides;
	}

	public void setProvides(List<Goal> provides) {
		this.provides = provides;
	}

	public List<Goal> getDepends() {
		if (this.depends == null) {
			this.depends = new ArrayList<>();
		}
		return depends;
	}

	public void setDepends(List<Goal> depends) {
		this.depends = depends;
	}
	
	public List<QualityParameter> getQualityParams() {
		if(qualityParams == null) {
			qualityParams = new ArrayList<>();
		}
		return qualityParams;
	}

	public void setQualityParams(List<QualityParameter> qualityParams) {
		this.qualityParams = qualityParams;
	}

	@Override
	public String toString() {
		return "Bundle [identification=" + identification + ", defines=" + defines + ", provides=" + provides +
				", conditions=" + conditions +  ", depends=" + depends + ", qualityParams=" + qualityParams
				+ "]";
	}
}
