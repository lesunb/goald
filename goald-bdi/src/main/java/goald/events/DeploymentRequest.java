package goald.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import goald.events.model.Change;
import net.mostlyoriginal.api.event.common.Event;

public class DeploymentRequest implements Event {

	public UUID target;

	List<Change> changes;
	
	public List<Change> getChanges() {
		if(changes == null){
			changes = new ArrayList<>();
		}
		return changes;
	}

	@Override
	public String toString() {
		return "DeploymentRequest [changes=" + changes + "]";
	}
	
}
