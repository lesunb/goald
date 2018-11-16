package goald.evaluation.tas;

import javax.inject.Singleton;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import goald.profile.Profile;
import goald.profile.ProfileBuilder;
import goald.profile.ProfileService;
import simpletas.definitions.ProvideHealthSuport;

@Singleton
public class TeleAssistanceBehaviourComparisionMain {


	public static void main(String[] args) throws Exception {
		System.out.println("Initializing TAS Behaviour Comparision  ... ");

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		
		Profile profile = ProfileBuilder.create()
				.forVariable("pick", "buttonMsg",  0.1f, "vitalParamsMsg", 0.9f)
				.forVariable("analyzeData", 
							"patientOK", 0.25f, 
							"changeDrug",0.25f, 
							"changeDose",0.25f, 
							"sendAlarm",0.25f )
				.withFailureRate("AlarmService1", 0.1f)
				.withFailureRate("AlarmService2", 0.1f)
				.withFailureRate("AlarmService3", 0.1f)
				.withFailureRate("MedicalService1", 0.1f)
				.withFailureRate("MedicalService2", 0.1f)
				.withFailureRate("MedicalService3", 0.1f)
				.withFailureRate("DrugService", 0.1f)
				.build();
		
		container.select(ProfileService.class).get()
		.setProfile(profile);
		
		//container.select(EvaluateStrategy.class).get()
		container.select(ProvideHealthSuport.class).get()
		.loop(3);

		container.shutdown();
		System.out.println("\n\n----");
		System.out.println("TAS Behaviour Comparision has come a normal end. Good bye");
	}
}
