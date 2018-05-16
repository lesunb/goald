package goalp.scalability;

import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Test;

import goalp.evaluation.goals.ICreateExperiments;
import goalp.evaluation.model.Experiment;
import goalp.evaluation.plans.CreateExperimentsToEvaluateScalabilityOverPlanSize;
import goalp.evaluation.plans.CreateExperimentsToEvaluateScalabilityOverVariabilityLevel;
import goalp.evaluation.strategy.EvaluateStrategy;

public class ScalabilityExperiments {

	@Test
	public void evaluateScalabilityOverPlanSize() {
		System.out.println("Initializing goalp planning evaluation ... ");

		// get experiments
		@SuppressWarnings("unchecked")
		List<Experiment> experiments = (List<Experiment>) getExperiments(
				CreateExperimentsToEvaluateScalabilityOverPlanSize.class).exec();
		exec(experiments);
	}

	@Test
	public void evaluateScalabilityVariabilityLevel() {
		System.out.println("Initializing goalp planning evaluation ... ");

		// get experiments
		@SuppressWarnings("unchecked")
		List<Experiment> experiments = (List<Experiment>) getExperiments(
				CreateExperimentsToEvaluateScalabilityOverVariabilityLevel.class).exec();
		exec(experiments);
	}

	public void exec(List<Experiment> experiments) {
		// intialize a CDI container
		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		// exec evaluation strategy
		container.select(EvaluateStrategy.class).get().exec(experiments);

		// shutdown container
		container.shutdown();

		System.out.println("Goalp planning evaluation has come a normal end. Good bye");
	}

	public ICreateExperiments getExperiments(Class<? extends ICreateExperiments> classCreator) {
		try {
			return (ICreateExperiments) classCreator.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
