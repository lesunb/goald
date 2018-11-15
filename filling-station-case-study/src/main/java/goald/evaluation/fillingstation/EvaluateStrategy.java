package goald.evaluation.fillingstation;

import java.util.List;

import javax.inject.Inject;

import goald.eval.exec.IExperimentsExecutor;
import goald.evaluation.response.ResponseEvaluation;
import goalp.evaluation.goals.IReportResult;

public class EvaluateStrategy  {

	@Inject
	IReportResult report;
	
	@Inject
	IExperimentsExecutor executor;
	
	public void exec() {
		
		try {
			List<ResponseEvaluation> evaluation= executor.exec();
			report.start(evaluation.size());
			report.doReport(evaluation.stream());
			report.flushAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}		
	}
}
