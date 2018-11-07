package goald.tas;

import javax.inject.Inject;

import goald.tas.definitions.HandleButtonPush;
import goald.tas.definitions.SendAlarm;

public class HandleButtonPushImpl implements HandleButtonPush {

	@Inject
	SendAlarm sendAlarm;
	
	@Override
	public void doHandle() {
		System.out.println("handling button pushed");
		sendAlarm.exec();
	}

}
