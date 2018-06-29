package goald.evaluation.strategy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;

import goald.eval.exec.Evaluation;
import goald.eval.spec.Experiment;
import goald.evaluation.exec.ExecuteExperiment;
import goalp.evaluation.goals.IEvaluate;
import goalp.evaluation.goals.IReportResult;

public class EvaluateStrategy implements IEvaluate {

	@Inject
	ExecuteExperiment execute;
	
	@Inject
	IReportResult report;
	
	@Override
	public void exec(List<Experiment> experiments){
		// Optional<Stream<Evaluation>> _result = 
		experiments
		.stream()
		.map(execute::accept)
		.forEach(report::doReport);
		
		report.flushAll();
//		.reduce((acc, result) -> {
//			System.out.println(result);
//			report.doReport(result);
//			return result;
//		});
		
//		long a = _result.get().count();
//		System.out.println(a);
//		.eac
//		.forEach(execute)
		//report.exec(experiments);
 	}	
}
