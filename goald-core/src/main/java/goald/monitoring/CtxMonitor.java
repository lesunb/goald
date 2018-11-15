package goald.monitoring;

import java.util.ArrayList;
import java.util.List;

import goald.model.ContextChange;

public class CtxMonitor {

	public List<ContextChange> changes;
	
	public int index;

	public List<ContextChange> getChange(Long currentTime) {
		List<ContextChange> identifiedChanges = new ArrayList<>();
		while(hasChange(currentTime)) {
			identifiedChanges.add(changes.get(index++));
		}
		return identifiedChanges;
	}
	
	public boolean hasChange(Long currentTime) {
		return index< changes.size() && 
				changes.get(index).getTime() <= currentTime;
	}
	
	public void addToChanges(ContextChange contextChange) {
		if(this.changes == null) {
			this.changes = new ArrayList<>();
		}
		this.changes.add(contextChange);
		this.changes.sort((change1, change2) -> { return (int) (change1.getTime() - change2.getTime());});
	}
}
