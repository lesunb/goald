package goald.events;

import java.util.UUID;

import net.mostlyoriginal.api.event.common.Event;

public class AddGoal implements Event  {

	public UUID target;
	
	public String identification;
}
