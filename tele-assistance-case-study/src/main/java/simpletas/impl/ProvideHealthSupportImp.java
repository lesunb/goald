package simpletas.impl;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.profile.ProfileService;
import goald.profile.TraceService;
import simpletas.definitions.HandleButtonPush;
import simpletas.definitions.ProvideAutomatedLifeSupport;
import simpletas.definitions.ProvideHealthSuport;

public class ProvideHealthSupportImp implements ProvideHealthSuport {

	@Inject
	private ProvideAutomatedLifeSupport provideAutomatedLifeSupport;

	@Inject
	private HandleButtonPush handleButtonPush;
	
	@Inject
	private ProfileService profileService;
	
	@Inject
	Logger log;
	
	@Inject
	private TraceService traceService;
	
	public void exec() {
		System.out.println("Initing Tele Assistance Service...");
		traceService.startNewExectution();
		String pick = getPick();
		try {
			if(pick=="vitalParamsMsg") {
				provideAutomatedLifeSupport.doHandle();
			}
			else if(pick=="buttonMsg") {
				handleButtonPush.doHandle();
			}			
		}catch(SystemException e) {
			//log.error(e.serviceName);
			traceService.error("system");
		}
		
		System.out.println(traceService.getTraces());
	}

	private String getPick() {
		//return "buttonMsg";
		return profileService.getValue("pick");
	}
	
	public void loop(int iterations) {
		for(int i=0; i<iterations; i++) {
			this.exec();
		}
	}
}
