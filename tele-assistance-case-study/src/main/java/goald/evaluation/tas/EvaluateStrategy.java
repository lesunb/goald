package goald.evaluation.tas;

import java.util.List;

import javax.inject.Inject;

import goald.evaluation.timeline.ReportTimelineEvaluationToFile;
import goald.evaluation.timeline.TimelineEvaluation;

public class EvaluateStrategy  {

	@Inject
	ReportTimelineEvaluationToFile report;
	
	@Inject
	TASAllContextsChangesScenarios executor;
	
	public void exec() {
		
		try {
			List<TimelineEvaluation> evaluation= executor.exec();
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
