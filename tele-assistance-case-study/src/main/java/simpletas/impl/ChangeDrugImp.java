package simpletas.impl;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.profile.CallFailure;
import goald.profile.ServicesCall;
import simpletas.definitions.ChangeDrug;

public class ChangeDrugImp implements ChangeDrug {

	@Inject
	private ServicesCall servicesCall;
	
	@Inject
	Logger log;
	
	@Override
	public void exec() {
		String[] services = { "DrugService" };
		try {
			servicesCall.call(services, 2);			
		} catch (CallFailure e) {
			log.info("service failure");
			throw new SystemException("ChangeDrug");
		}
	}	
}
