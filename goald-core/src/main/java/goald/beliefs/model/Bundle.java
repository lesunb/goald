package goald.beliefs.model;

import java.util.ArrayList;
import java.util.List;

import goald.desires.model.Goal;
import goald.model.ContextCondition;
import goald.model.Contribution;

public class Bundle {

	public String uuid;

	public String identification;

	protected List<Goal> defines;

	protected List<ContextCondition> conditions;

	protected List<Goal> provides;

	protected List<Goal> depends;

	protected List<Contribution> contribute;

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

	public List<Contribution> getContribute() {
		if (this.contribute == null) {
			this.contribute = new ArrayList<>();
		}
		return contribute;
	}

	public void setContribute(List<Contribution> contribute) {
		this.contribute = contribute;
	}

}
