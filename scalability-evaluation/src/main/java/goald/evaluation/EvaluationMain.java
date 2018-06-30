package goald.evaluation;

import java.util.List;

import javax.inject.Singleton;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import goald.eval.spec.Experiment;
import goald.evaluation.strategy.EvaluateStrategy;
import goald.experiments.CreateExperimentsToEvaluateScalabilityOverPlanSize;
import goald.experiments.CreateExperimentsToEvaluateScalabilityOverVariabilityLevel;
import goalp.evaluation.goals.ICreateExperiments;

@Singleton
public class EvaluationMain {


	public static void main(String[] args) {


		System.out.println("Initializing goalp planning evaluation ... ");

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();

		// execute experiments
		container.select(EvaluateStrategy.class).get()
			.exec(getExperiments(
					CreateExperimentsToEvaluateScalabilityOverPlanSize.class));
		
		container.select(EvaluateStrategy.class).get()
			.exec(getExperiments(
					CreateExperimentsToEvaluateScalabilityOverVariabilityLevel.class));
		
		container.shutdown();

		System.out.println("Goalp planning evaluation has come a normal end. Good bye");
	}

	public static List<Experiment> getExperiments(Class<? extends ICreateExperiments> classCreator){
		try {
			ICreateExperiments creator = classCreator.newInstance();
			return creator.exec();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
