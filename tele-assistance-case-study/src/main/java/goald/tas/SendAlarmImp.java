package goald.tas;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.profile.CallFailure;
import goald.profile.ServicesCall;
import goald.tas.definitions.SendAlarm;

public class SendAlarmImp implements SendAlarm {

	@Inject
	private ServicesCall servicesCall;
	
	@Inject
	Logger log;
	
	
	@Override
	public void exec() {
		
		String[] services = {"alarmService1"
                , "alarmService2", "alarmService3" };
		
		try {
			servicesCall.call(services, 2);
		} catch (CallFailure e) {
			log.info("service failure");
			throw new SystemException("alarmService");
		}
	}

}
