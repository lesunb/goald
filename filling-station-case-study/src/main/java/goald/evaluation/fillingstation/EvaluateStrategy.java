package goald.evaluation.fillingstation;

import java.util.List;

import javax.inject.Inject;

import goald.eval.exec.Evaluation;
import goalp.evaluation.goals.IReportResult;

public class EvaluateStrategy  {

	@Inject
	IReportResult report;
	
	@Inject
	IExperimentsExecutor executor;
	
	public void exec() {
		try {
			List<Evaluation> evaluation= executor.exec();
			report.doReport(evaluation.stream());
			report.flushAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}		
	}
}
