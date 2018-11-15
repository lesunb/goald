package goald.evaluation.timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Named;

import goald.evaluation.Evaluation;
import goald.evaluation.Measure;
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
		
		List<String> lines = getLines(evaluation);
		lines.addAll(lines);
		this.flushAll();
	}
	
	@Override
	public void doReport(Stream<TimelineEvaluation> evaluations) {

		evaluations.forEach(this::addToReport);
		
	}
	
	private boolean initLinesWithHeader(Evaluation eval) {
		if(eval == null) {
			return false;
		}
		
		StringBuffer sb = new StringBuffer();
		eval.getFactors().entrySet()
		.forEach(entry ->{
			sb.append(entry.getKey() + "\t");
		});
		
		sb.append("execIndex\t");
		
//		Map<Integer, List<Measure>> indexedMesures  = eval.getIndexedMeasures();
//		Integer firstKey = (Integer) indexedMesures.keySet().toArray()[0];
//		
//		List<Measure> firstSetOfMeasures = indexedMesures.get(firstKey);
//		
//		
//		for(Measure measure: firstSetOfMeasures) {
//			String measurelabel = measure.getLabel();
//			sb.append(measurelabel + "\t");
//		}
		sb.append("\r\n");		
		this.lines = new ArrayList<>();
		lines.add(sb.toString());
		return true;
	}
	

	private List<String> getLines(Evaluation eval) {
		StringBuffer sb = new StringBuffer();
		List<String> lines = new ArrayList<>();
		eval.getFactors().entrySet()
		.forEach(entry ->{
			sb.append(entry.getValue() + "\t");
		});
			
		StringBuffer lineBuff = new StringBuffer();
//		for(Map.Entry<Integer,List<Measure>> measureEntry: eval.getIndexedMeasures().entrySet()) {
//			
//			// append all factors
//			lineBuff.append(sb);
//			
//			// append execution index (id of a repetition with same factors)
//			lineBuff.append(measureEntry.getKey()+ "\t");
//			
//			// append each measure
//			measureEntry.getValue().forEach( measure ->{
//				lineBuff.append(measure.getValue()+ "\t");
//			});
//			// end of line
//			lineBuff.append("\r\n");
//			
//			//add to lines set ant reset linebuff to reuse
//			lines.add(lineBuff.toString());
//			lineBuff.setLength(0); 
//		}
		return lines;
	}
}
