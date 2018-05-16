package goald.dam.model;

import java.util.ArrayList;
import java.util.List;

public class Dame {
	private Bundle definition;
	
	private Alternative parentAlt;
	
	private List<Alternative> alts;
	
	private Alternative chosenAlt;

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
	
}
