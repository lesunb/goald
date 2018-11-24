package goald.evaluation.tas.asert;

import java.util.List;

import javax.inject.Inject;

import goald.evaluation.response.ReportResponseEvaluationToFile;
import goald.evaluation.response.ResponseEvaluation;

public class TasAssertEvaluateStrategy  {

	@Inject
	ReportResponseEvaluationToFile report;
	
//	@Inject
//	TASAllContextsChangesScenarios executor;

	@Inject
	TASAssertAllContextsChangesScenarios executor;

	public void exec() {
		
		try {
			List<ResponseEvaluation> evaluation= executor.exec();
			report.start(evaluation.size());
			report.doReport(evaluation.stream());
			report.flushAll();
			report.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}		
	}
}
