package goald.tas;

import goald.tas.definitions.SendAlarm;

public class SendAlarmImp implements SendAlarm {

	@Override
	public void exec() {
		System.out.println("sending alarm message");
	}

}
