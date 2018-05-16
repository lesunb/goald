package goalp.evaluation.plans;

import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.panayotis.gnuplot.dataset.Point;

import goalp.Conf;
import goalp.Conf.Keys;
import goalp.evaluation.model.Execution;
import goalp.evaluation.model.PlanningExperiment;
import goalp.exputil.DataSetBuilder;
import goalp.exputil.EvalUtil;
import goalp.exputil.PlotBuilder;

/**
 * Context Requirement: gnuplot installed
 */
//@Named
public class ReportAllExperimentsResultsToOneEpsGraph { // implements IReportResult {

	@Inject
	Logger log;

	//@Override
	public void exec(List<PlanningExperiment> experiments) {
		
		// create graph
		PlotBuilder plotBuilder = PlotBuilder.create()
			.asEps(Conf.get(Keys.RESULT_FILE) + EvalUtil.getOneFactor(experiments.get(0)) + ".eps")
			.xLabel(EvalUtil.getOneFactor(experiments.get(0)))
			.yLabel(EvalUtil.getResponseVariable(experiments.get(0)));

		experiments.forEach((exp) -> {
			List<String> factors = exp.getEvaluation().getFactorList();
			if (factors.size() == 0) {
				throw new IllegalStateException("experiment factors was not initialized");
			} else if (factors.size() == 1) {

				String factor = factors.get(0);
				String responseVariable = ((PlanningExperiment) exp).getEvaluation().getResponseVariable();
				log.info("ploting {} vs {}", factor, responseVariable);

				// create graph (factor vs result) for the experiment execution
				// list
				addDataSet(exp, plotBuilder, (exec) -> {
					Number factorValue = exec.getEvaluation().getFactors().get(factor);
					Number response = exec.getEvaluation().getResponseValue();
					return new Point<Number>(factorValue, nanoToMiliseconds(response));
				});

			} else if (factors.size() > 1) {
				throw new IllegalStateException("multi factors experiment report was requested but implemented");
			}

		});
		
		plotBuilder.plot();
	}

	private void addDataSet(PlanningExperiment exp, PlotBuilder plotBuilder, Function<Execution, Point<Number>> mapper) {

		// create dataset
		DataSetBuilder<Number> dsbuilder = DataSetBuilder.create();

		for (Execution exec : exp.getExecutions()) {

			dsbuilder.addPoint(mapper.apply(exec));
		}

		// add data set
		plotBuilder.addDataSet(dsbuilder.buildDataSetPlot());
	}

	private Double nanoToMiliseconds(Number nanoSecs) {
		Double value = (1.0d / 1000.0d);
		value *= nanoSecs.longValue();
		return value;
	}
}
