package goald.evaluation.timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.inject.Named;

import goald.evaluation.ReportToFileAbstract;

@Named
public class ReportTimelineEvaluationToFile extends ReportToFileAbstract<TimelineEvaluation> {

	public ReportTimelineEvaluationToFile() {
		
	}

	@Override
	public void addToReport(TimelineEvaluation evaluation) {
		if(lines == null) {
			initLinesWithHeader(evaluation);
			this.initFile(evaluation);
		}
		
		List<String> evalLines = getLines(evaluation);
		lines.addAll(evalLines);
		this.flushAll();
	}
	
	@Override
	public void doReport(Stream<TimelineEvaluation> evaluations) {

		evaluations.forEach(this::addToReport);
		
	}
	
	private boolean initLinesWithHeader(TimelineEvaluation eval) {
		if(eval == null) {
			return false;
		}
		
		StringBuffer sb = new StringBuffer();
		eval.getFactors().entrySet()
		.forEach(entry ->{
			sb.append(entry.getKey() + "\t");
		});
		
		sb.append("execIndex");
		sb.append("\tlabel");
		sb.append("\tstart");
		sb.append("\tend");
		sb.append("\r\n");
		
		this.lines = new ArrayList<>();
		lines.add(sb.toString());
		return true;
	}
	

	private List<String> getLines(TimelineEvaluation eval) {
		StringBuffer sb = new StringBuffer();
		List<String> lines = new ArrayList<>();
		eval.getFactors().entrySet()
		.forEach(entry ->{
			sb.append(entry.getValue() + "\t");
		});
			
		StringBuffer lineBuff = new StringBuffer();
		StringBuffer header = new StringBuffer();
		if(eval.getIndexedMeasures() == null) {
			return lines;
		}
		for(Entry<Integer, List<TimelineMeasure>> measureEntry: eval.getIndexedMeasures().entrySet()) {
			
			// append all factors
			header.append(sb);
			
			// append execution index (id of a repetition with same factors)
			header.append(measureEntry.getKey()+ "\t");
			
			
			// append each measure time line format (label, start, end)
			measureEntry.getValue().forEach( measure ->{
				lineBuff.append(header);
				lineBuff.append(measure.getLabel()+ "\t");
				lineBuff.append(measure.getStart()+ "\t");
				lineBuff.append(measure.getEnd());
				// end of line
				//add to lines set ant reset linebuff to reuse
				lineBuff.append("\r\n");
				lines.add(lineBuff.toString());
				lineBuff.setLength(0); 
			});
		}
		return lines;
	}
}
