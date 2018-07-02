package goald.evaluation;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.Execution;
import goald.eval.spec.Experiment;
import goald.evaluation.exec.ExecuteExperiment;
import goald.exputil.EvalUtil;
import goalp.evaluation.goals.IEvaluate;
import goalp.evaluation.goals.IReportResult;

public class EvaluateStrategy implements IEvaluate {

	@Inject
	ExecuteExperiment execute;
	
	@Inject
	IReportResult report;
	
	@Inject
	Logger log;
	
	@Inject
	Timer timer;
	
	@Override
	public void exec(List<Experiment> experiments){
		experiments.forEach( experiment -> {
			execute.setup(experiment);
			log.info("Experiment factor {}", EvalUtil.getFactors(experiment));
			
			//exec
			List<Execution> execs = experiment.getExecutions();
			int total = execs.size();
			report.start(total);
			
			execs.stream()
			.map((exec) -> {
				execute.execute(exec.getSpecification(), exec.getEvaluation());
				return exec.getEvaluation();
			})
			.forEach(report::addToReport);
			 
			 report.flushAll();
		});
 	}	
}
