package goald.profile;

import javax.inject.Inject;

public class ServicesCall  {

	@Inject
	private ProfileService profileService;
	
	@Inject
	private TraceService traceService;
	
	
	public void call(String[] services, int retries) throws CallFailure {
		for(String serviceName: services) {
			for(int i=0; i<=retries; i++) {
				if(profileService.checkSuccess(serviceName)) {
					traceService.succes(serviceName);
					return;
				}
				traceService.error(serviceName);
			}
		}
		throw new CallFailure(services[0] + " and others");
	}
}
