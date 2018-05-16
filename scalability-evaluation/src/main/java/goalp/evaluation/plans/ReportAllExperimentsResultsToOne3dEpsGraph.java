package goalp.evaluation.plans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.style.FillStyle.Fill;
import com.panayotis.gnuplot.style.Style;

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
public class ReportAllExperimentsResultsToOne3dEpsGraph {// implements IReportResult {

	@Inject
	Logger log;

	//@Override
	public void exec(List<PlanningExperiment> experiments) {
		
		// create graph
		PlotBuilder plotBuilder = PlotBuilder.create()
			.asEps(Conf.get(Keys.RESULT_FILE) + EvalUtil.getFactor(experiments.get(0),0) + ".eps")
			.xLabel(EvalUtil.getFactor(experiments.get(0), 0))
			.xRange(Double.NaN, Double.NaN, true)
			//.yRange(0d, 10d, false)
			.yLabel(EvalUtil.getFactor(experiments.get(0), 1))
			.zLabel(EvalUtil.getResponseVariable(experiments.get(0)))
			.zTicslevel(0)
			.xyplane(0);

		experiments.forEach((exp) -> {
			List<String> factors = exp.getEvaluation().getFactorList();
			if (factors.size() == 0) {
				throw new IllegalStateException("experiment factors was not initialized");
			} else if (factors.size() != 2) {
				throw new IllegalStateException("3d graph only supported for two factors experiments");
			} else if (factors.size() == 2) {

				String factorOne = factors.get(0);
				String factorTwo = factors.get(1);
				
				Map<Integer, List<Execution>> contextExecutions= new HashMap<>();
				for(Execution exec:exp.getExecutions()){
					Integer variability = exec.getSpecification().getInteger("variability");
					List<Execution> execListPerVariability = contextExecutions.get(variability);
					if(execListPerVariability == null){
						execListPerVariability = new ArrayList<>();
						contextExecutions.put(exec.getSpecification().getInteger("variability"),
								execListPerVariability);
					}
					
					execListPerVariability.add(exec);
					
				}
				
				String responseVariable = ((PlanningExperiment) exp).getEvaluation().getResponseVariable();
				//log.info("ploting {},{} vs {}", factorOne, factorTwo, responseVariable);

				// create graph (factor vs r)esult) for the experiment execution
				// list
				for(List<Execution> execs: contextExecutions.values()){
					addDataSet(exp, execs, plotBuilder, (exec) -> {
						Number factorOneValue = exec.getEvaluation().getFactors().get(factorOne);
						Number factorTwoValue = exec.getEvaluation().getFactors().get(factorTwo);
						Number response = exec.getEvaluation().getResponseValue();
						return new Point<Number>(factorOneValue, factorTwoValue, nanoToMiliseconds(response));
					});
				}
			} 

		});
		
		plotBuilder.plot();
	}

	private void addDataSet(PlanningExperiment exp,  List<Execution> execs, PlotBuilder plotBuilder, Function<Execution, Point<Number>> mapper) {

		// create dataset
		DataSetBuilder<Number> dsbuilder = 
				DataSetBuilder.create()
				.setStyle(Style.BOXES, Fill.PATTERN);

		for (Execution exec : execs) {

			dsbuilder.addPoint(mapper.apply(exec));
		}

		// add data set
		plotBuilder.addDataSet(dsbuilder.buildDataSetPlot());
	}

	private Double nanoToMiliseconds(Number nanoSecs) {
		Double value = (nanoSecs.longValue()/1000000d);
		return value;
	}
}
