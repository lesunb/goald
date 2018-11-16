package goald.evaluation.fillingstation;

import java.util.List;

import javax.inject.Inject;

import goald.evaluation.response.ReportResponseEvaluationToFile;
import goald.evaluation.response.ResponseEvaluation;

public class EvaluateStrategy  {

	@Inject
	ReportResponseEvaluationToFile report;
	
	@Inject
	FillingStationStudyCaseScenarios executor;
	
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
