package goald.tas;

import javax.inject.Inject;

import goald.profile.ProfileService;
import goald.tas.definitions.AnalyzeData;

public class AnalyzeDataImp implements AnalyzeData {

	@Inject
	private ProfileService profileService;
	
	@Override
	public String doAnalyze(Double[] data) {
		return profileService.getValue("analyzeData");
	}

}
