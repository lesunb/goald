package simpletas.impl;

import javax.inject.Inject;

import org.slf4j.Logger;

import simpletas.definitions.AnalyzeData;
import simpletas.definitions.ChangeDose;
import simpletas.definitions.ChangeDrug;
import simpletas.definitions.GetVitalParams;
import simpletas.definitions.ProvideAutomatedLifeSupport;
import simpletas.definitions.SendAlarm;

public class ProvideAutomatedLifeSupportImpl implements ProvideAutomatedLifeSupport {

	@Inject
	private GetVitalParams getVitalParams;
	
	@Inject
	private AnalyzeData analyzeData;
	
	@Inject
	private ChangeDose changeDose;
	
	@Inject
	private ChangeDrug changeDrug;
	
	@Inject
	private SendAlarm sendAlarm;
	
	@Inject
	Logger log;

	@Override
	public void doHandle() {
		System.out.println("providing automated life support...");
		
		Double[] data = getVitalParams.doGet();
		
		String analysisResult = analyzeData.doAnalyze(data);
		
		switch (analysisResult) {
			case "changeDrug":
				log.debug("changeDrug");
				changeDrug.exec();
				break;
				
			case "changeDose":
				log.debug("changeDose");
				changeDose.exec();
				break;
				
			case "sendAlarm":
				log.debug("sendAlarm");
				sendAlarm.exec();
				break;
				
			case "patientOK":
				log.debug("patientOK. Do nothing");
				break;
		}
		
	}
	
}
