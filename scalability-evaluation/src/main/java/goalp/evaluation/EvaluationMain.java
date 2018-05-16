package goalp.evaluation;

import java.util.List;

import javax.inject.Singleton;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import goalp.evaluation.goals.ICreateExperiments;
import goalp.evaluation.model.Experiment;
import goalp.evaluation.plans.CreateExperimentsToEvaluateScalabilityOverPlanSize;
import goalp.evaluation.plans.CreateExperimentsToEvaluateScalabilityOverVariabilityLevel;
import goalp.evaluation.strategy.EvaluateStrategy;

@Singleton
public class EvaluationMain {


	public static void main(String[] args) {


		System.out.println("Initializing goalp planning evaluation ... ");

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();

		container.select(EvaluateStrategy.class).get()
		.exec(getExperiments(CreateExperimentsToEvaluateScalabilityOverPlanSize.class));
		
		container.select(EvaluateStrategy.class).get()
			.exec(getExperiments(CreateExperimentsToEvaluateScalabilityOverVariabilityLevel.class));
		
		container.shutdown();

		System.out.println("Goalp planning evaluation has come a normal end. Good bye");
	}

	public static List<Experiment> getExperiments(Class<? extends ICreateExperiments> classCreator){
		try {
			ICreateExperiments creator = (ICreateExperiments) classCreator.newInstance();
			return creator.exec();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
