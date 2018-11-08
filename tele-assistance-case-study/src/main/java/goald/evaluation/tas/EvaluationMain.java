package goald.evaluation.tas;

import javax.inject.Singleton;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import goald.profile.Profile;
import goald.profile.ProfileBuilder;
import goald.profile.ProfileService;
import goald.tas.ProvideHealthSupportImp;

@Singleton
public class EvaluationMain {


	public static void main(String[] args) throws Exception {
		System.out.println("Initializing goald planning evaluation ... ");

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		
		Profile profile = ProfileBuilder.create()
				.forVariable("pick", "buttonMsg",  0.9f, "vitalParamsMsg", 0.9f)
				.forVariable("analyzeData", "patientOK", 0.25f, "changeDrug",0.25f, 
						"changeDose",0.25f, "sendAlarm",0.25f  )
				
				//.forVariable("getVitalParams", 0.25f, "changeDrug",  0.75f, "buttonMsg")
				.withFailureRate("alarmService1", 0.99f)
				.withFailureRate("alarmService2", 0.99f)
				.withFailureRate("alarmService3", 0.99f)
				.build();
		
		container.select(ProfileService.class).get()
		.setProfile(profile);
		
		//container.select(EvaluateStrategy.class).get()
		container.select(ProvideHealthSupportImp.class).get()
		.exec();

		container.shutdown();

		System.out.println("Goald planning evaluation has come a normal end. Good bye");
	}
}
