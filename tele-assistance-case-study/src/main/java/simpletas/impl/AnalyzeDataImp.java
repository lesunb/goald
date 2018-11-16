package simpletas.impl;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.profile.CallFailure;
import goald.profile.ProfileService;
import goald.profile.ServicesCall;
import simpletas.definitions.AnalyzeData;

public class AnalyzeDataImp implements AnalyzeData {

	@Inject
	private ProfileService profileService;
	
	@Inject
	private ServicesCall servicesCall;
	
	@Inject
	Logger log;
	
	@Override
	public String doAnalyze(Double[] data) {

		
		String[] services = {
				"MedicalService1", 
				"MedicalService2", 
				"MedicalService3" };
		try {
			
			servicesCall.call(services, 2);
			return profileService.getValue("analyzeData");
			
		} catch (CallFailure e) {
			log.info("service failure");
			throw new SystemException("alarmService");
		}
		
	}

}
