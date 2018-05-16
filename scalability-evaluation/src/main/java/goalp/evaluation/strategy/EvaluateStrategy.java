package goalp.evaluation.strategy;

import java.util.List;

import javax.inject.Inject;

import goalp.evaluation.goals.IEvaluate;
import goalp.evaluation.goals.IExecuteExperiments;
import goalp.evaluation.goals.IReportResult;
import goalp.evaluation.model.Experiment;

public class EvaluateStrategy implements IEvaluate {

	@Inject
	IExecuteExperiments execute;
	
	@Inject
	IReportResult report;
	
	@Override
	public void exec(List<Experiment> experiments){
		experiments.forEach(execute);
		report.exec(experiments);
 	}
	
}
