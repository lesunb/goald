package goalp.evaluation.plans;

import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;

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
@Named
public class ReportEachResultToEpsGraph {// implements IReportResult {

	@Inject
	Logger log;

	//@Override
	public void exec(List<PlanningExperiment> experiments) {
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
				createGraphOneFactor(exp, (exec) -> {
					Number factorValue = exec.getEvaluation().getFactors().get(factor);
					Number response = exec.getEvaluation().getResponseValue();
					return new Point<Number>(factorValue, nanoToMiliseconds(response));
				});

			} else if (factors.size() == 2) {
				
				String factor1 = factors.get(0);
				String factor2 = factors.get(2);
				String responseVariable = ((PlanningExperiment) exp).getEvaluation().getResponseVariable();
				log.info("ploting {},{} = {}", factor1, factor2, responseVariable);

				// create graph (factor vs result) for the experiment execution
				// list
				createGraphTwoFactor(exp, (exec) -> {
					Number factorXValue = exec.getEvaluation().getFactors().get(0);
					Number factorYValue = exec.getEvaluation().getFactors().get(1);
					Number response = exec.getEvaluation().getResponseValue();
					return new Point<Number>(factorXValue, factorYValue, nanoToMiliseconds(response));
				});
				
			} else if (factors.size() > 1) {
				throw new IllegalStateException("multi factors experiment report was requested but implemented");
			}

		});
	}

	private void createGraphOneFactor(PlanningExperiment exp, Function<Execution, Point<Number>> mapper) {

		// create dataset
		DataSetBuilder<Number> dsbuilder = DataSetBuilder.create();

		for (Execution exec : exp.getExecutions()) {

			dsbuilder.addPoint(mapper.apply(exec));
		}
		// dsbuilder.fromNanoSecondsToHumanReadable(1, TimeUnit.NANOSECONDS);

		// create graph
		PlotBuilder.create()
			.asEps(Conf.get(Keys.RESULT_FILE) + EvalUtil.getOneFactor(exp) + ".eps")
			.xLabel(EvalUtil.getOneFactor(exp))
			.yLabel(EvalUtil.getResponseVariable(exp))
			.addDataSet(dsbuilder.buildDataSetPlot())
			.plot();

	}

	private void createGraphTwoFactor(PlanningExperiment exp, Function<Execution, Point<Number>> mapper) {

		// create dataset
		DataSetBuilder<Number> dsbuilder = DataSetBuilder.create();

		for (Execution exec : exp.getExecutions()) {

			dsbuilder.addPoint(mapper.apply(exec));
		}
		// dsbuilder.fromNanoSecondsToHumanReadable(1, TimeUnit.NANOSECONDS);

		// create graph
		PlotBuilder.create()
			.asEps(Conf.get(Keys.RESULT_FILE) + EvalUtil.getFactor(exp, 0) + "_" + EvalUtil.getFactor(exp, 0) + ".eps")
			.xLabel(EvalUtil.getFactor(exp, 0))
			.yLabel(EvalUtil.getFactor(exp, 1))
			.zLabel(EvalUtil.getResponseVariable(exp))
			.addDataSet(dsbuilder.buildDataSetPlot())
			.plot();

	}

	private Double nanoToMiliseconds(Number nanoSecs) {
		Double value = (1.0d / 1000.0d);
		value *= nanoSecs.longValue();
		return value;
	}
}
