package simpletas.impl;

import javax.inject.Inject;

import simpletas.definitions.HandleButtonPush;
import simpletas.definitions.SendAlarm;

public class HandleButtonPushImpl implements HandleButtonPush {

	@Inject
	SendAlarm sendAlarm;
	
	@Override
	public void doHandle() {
		System.out.println("handling button pushed");
		sendAlarm.exec();
	}

}
