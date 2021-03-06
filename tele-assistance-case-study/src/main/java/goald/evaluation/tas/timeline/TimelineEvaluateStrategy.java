package goald.evaluation.tas.timeline;

import java.util.List;

import javax.inject.Inject;

import goald.evaluation.timeline.ReportTimelineEvaluationToFile;
import goald.evaluation.timeline.TimelineEvaluation;

public class TimelineEvaluateStrategy  {

	@Inject
	ReportTimelineEvaluationToFile report;
	
	@Inject
	TASTimelineStudyCaseScenarios executor;

	public void exec() {
		
		report.setMeasureOrder("context", "bundle", "system", "failure");
		
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
