package goald.model;

import java.util.ArrayList;
import java.util.List;

public class Dame {
	private Bundle definition;
	
	private Alternative parentAlt;
	
	private List<Alternative> alts;
	
	private Alternative chosenAlt;
	
	private Boolean isAchievable;

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

	public Boolean getIsAchievable() {
		return isAchievable;
	}

	public void setIsAchievable(Boolean isAchievable) {
		this.isAchievable = isAchievable;
	}
	
	@Override
	public String toString() {
		return "Dame [definition=" + definition + ", chosenAlt=" + chosenAlt + ", alts=" + alts + "]";
	}
}
