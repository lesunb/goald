package goalp.evaluation.fillingstation;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

@Singleton
public class EvaluationMain {


	public static void main(String[] args) throws Exception {


		System.out.println("Initializing goalp planning evaluation ... ");

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		container.select(FillingStationStudyCase.class).get().exec();
		container.shutdown();

		System.out.println("Goalp planning evaluation has come a normal end. Good bye");
	}

	public void printHello(@Observes ContainerInitialized event, @Parameters List<String> parameters) {

		System.out.println("Hello");

	}
}
