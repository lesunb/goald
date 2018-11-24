package goald.evaluation.tas.asert;

import javax.inject.Singleton;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import goald.evaluation.tas.asert.TasAssertEvaluateStrategy;

@Singleton
public class TASMain {


	public static void main(String[] args) throws Exception {
		System.out.println("Initializing goald planning evaluation ... ");

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		
		container.select(TasAssertEvaluateStrategy.class).get()
		.exec();

		container.shutdown();

		System.out.println("Goald planning evaluation has come a normal end. Good bye");
	}
}
