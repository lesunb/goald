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

import goalp.Conf;
import goalp.Conf.Keys;
import goalp.evaluation.goals.IReportResult;
import goalp.evaluation.model.Execution;
import goalp.evaluation.model.Experiment;
import goalp.exputil.DataSetBuilder;
import goalp.exputil.EvalUtil;
import goalp.exputil.GnuPlotDataBuilder;

/**
 * Context Requirement: gnuplot installed
 */
@Named
public class ReportAllExperimentsResultsToText implements IReportResult {

	@Inject
	Logger log;

	@Override
	public void exec(List<Experiment> experiments) {
		
		// create graph
		GnuPlotDataBuilder dataSetFileBuilder = GnuPlotDataBuilder.create()
				.toFile(Conf.get(Keys.RESULT_FILE) + EvalUtil.getFactor(experiments.get(0),0) + ".dat");

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
				
				for(List<Execution> execs: contextExecutions.values()){
					addDataSet(exp, execs, dataSetFileBuilder, (exec) -> {
						Number factorOneValue = exec.getEvaluation().getFactors().get(factorOne);
						Number factorTwoValue = exec.getEvaluation().getFactors().get(factorTwo);
						Number response = exec.getEvaluation().getResponseValue();
						return new Point<Number>(factorOneValue, factorTwoValue, nanoToMiliseconds(response));
					});
				}
			} 

		});
		
		dataSetFileBuilder.done();
	}

	private void addDataSet(Experiment exp,  List<Execution> execs, GnuPlotDataBuilder plotBuilder, Function<Execution, Point<Number>> mapper) {

		// create dataset
		DataSetBuilder<Number> dsbuilder = DataSetBuilder.create();

		for (Execution exec : execs) {

			dsbuilder.addPoint(mapper.apply(exec));
		}

		// add data set
		plotBuilder.addDataSet(dsbuilder.buildPointDataSet());
	}

	private Double nanoToMiliseconds(Number nanoSecs) {
		Double value = (nanoSecs.longValue()/1000000d);
		return value;
	}
}
