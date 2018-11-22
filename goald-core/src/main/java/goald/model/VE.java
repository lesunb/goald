package goald.model;

import java.util.ArrayList;
import java.util.List;

public class VE {
	private Bundle definition;
	
	private Alternative parentAlt;
	
	private List<Alternative> alts;
	
	private Alternative chosenAlt;
	
	private Boolean isAchievable;
	
	private Dependency satisfy;

	public Bundle getDefinition() {
		return definition;
	}

	public void setDefinition(Bundle definition) {
		this.definition = definition;
	}

	public Alternative getParentAlt() {
		return parentAlt;
	}

	public void setParentAlt(Alternative parentAlt) {
		this.parentAlt = parentAlt;
	}

	public List<Alternative> getAlts() {
		if(alts == null) {
			alts = new ArrayList<>();
		}
		return alts;
	}

	public void setAlts(List<Alternative> alts) {
		this.alts = alts;
	}

	public Alternative getChosenAlt() {
		return chosenAlt;
	}

	public void setChosenAlt(Alternative chosenAlt) {
		this.chosenAlt = chosenAlt;
	}

	public Boolean isAchievable() {
		return isAchievable;
	}
	
	public void setIsAchievable(Boolean isAchievable) {
		this.isAchievable = isAchievable;
	}
	
	public Dependency getSatisfy() {
		return satisfy;
	}

	public void setSatisfy(Dependency satisfy) {
		this.satisfy = satisfy;
	}
	
	@Override
	public String toString() {
		return "VE [ satisfy="+ satisfy +", definition=" + definition + ", chosenAlt=" + chosenAlt + ", alts=" + alts + "]";
	}

}
