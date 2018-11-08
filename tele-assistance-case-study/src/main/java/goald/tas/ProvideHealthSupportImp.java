package goald.tas;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.profile.ProfileService;
import goald.tas.definitions.HandleButtonPush;
import goald.tas.definitions.ProvideAutomatedLifeSupport;
import goald.tas.definitions.ProvideHealthSuport;

public class ProvideHealthSupportImp implements ProvideHealthSuport {

	@Inject
	private ProvideAutomatedLifeSupport provideAutomatedLifeSupport;

	@Inject
	private HandleButtonPush handleButtonPush;
	
	@Inject
	private ProfileService profileService;
	
	@Inject
	Logger log;
	
	public void exec() {
		System.out.println("Initing Tele Assistance Service...");
		
		String pick = getPick();
		try {
			if(pick=="vitalParamsMsg") {
				provideAutomatedLifeSupport.doHandle();
			}
			else if(pick=="buttonMsg") {
				handleButtonPush.doHandle();
			}			
		}catch(SystemException e) {
			log.error(e.serviceName);
		}
	}

	private String getPick() {
		//return "buttonMsg";
		return profileService.getValue("pick");
	}
}
