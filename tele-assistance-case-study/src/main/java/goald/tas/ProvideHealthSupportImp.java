package goald.tas;

import javax.inject.Inject;

import goald.tas.definitions.HandleButtonPush;
import goald.tas.definitions.ProvideAutomatedLifeSupport;
import goald.tas.definitions.ProvideHealthSuport;

public class ProvideHealthSupportImp implements ProvideHealthSuport {

	@Inject
	private ProvideAutomatedLifeSupport provideAutomatedLifeSupport;

	@Inject
	private HandleButtonPush handleButtonPush;
	
	public void exec() {
		System.out.println("Initing Tele Assistance Service...");
		
		String pick = getPick();
		
		if(pick=="vitalParamsMsg") {
			provideAutomatedLifeSupport.doHandle();
		}
		else if(pick=="buttonMsg") {
			handleButtonPush.doHandle();
		}
	}

	private String getPick() {
		//return "buttonMsg";
		return "vitalParamsMsg";
	}
}
