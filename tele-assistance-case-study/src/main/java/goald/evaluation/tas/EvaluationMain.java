package goald.evaluation.tas;

import javax.inject.Singleton;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import goald.tas.ProvideHealthSupportImp;

@Singleton
public class EvaluationMain {


	public static void main(String[] args) throws Exception {
		System.out.println("Initializing goald planning evaluation ... ");

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		
		//container.select(EvaluateStrategy.class).get()
		container.select(ProvideHealthSupportImp.class).get()
		.exec();

		container.shutdown();

		System.out.println("Goald planning evaluation has come a normal end. Good bye");
	}
}
