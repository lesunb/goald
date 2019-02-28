package goald.evaluation.timeline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;

import goald.evaluation.ReportToFileAbstract;

@Named
public class ReportTimelineEvaluationToFile extends ReportToFileAbstract<TimelineEvaluation> {

	Comparator<? super TimelineMeasure> compareType = (e1, e2)-> e1.compareTo(e2);
	
	
	public ReportTimelineEvaluationToFile() {
		
	}
	
	public void setMeasureOrder(String ...types) {
		Map<String, Integer> orderMap = new HashMap<>();
		int index = 0;
		for(String type: types) {
			orderMap.put(type, index++);
		}
		
		this.compareType = (e1, e2)->
			10*(orderMap.get(e1.getType()) - orderMap.get(e2.getType()));
			//+ e1.compareTo(e2);
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

		evaluations
		.forEach(this::addToReport);
		
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
		sb.append("\tplotIndex");
		sb.append("\tlabel");
		sb.append("\tstart");
		sb.append("\tend");
		sb.append("\ttype");
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
						
			//complement of negated contexts
			Map<String, List<TimelineMeasure>> negatedMeasuresMap = new HashMap<>();
			measureEntry.getValue().stream()
			.filter( measure ->
				measure.getType() == "context" && measure.getLabel().startsWith("!")
			).forEach( measure -> {
				List<TimelineMeasure> measures = negatedMeasuresMap.get(measure.getLabel());
				if(measures == null) {
					measures = new ArrayList<TimelineMeasure>();
					negatedMeasuresMap.put(measure.getLabel(), measures);
				}
				measures.add(measure);
			});
			
			negatedMeasuresMap.forEach( (key, value)-> {
				System.out.println(key.substring(1));
				Stream<TimelineMeasure> negatedContexSerie = getComplementar(value.stream(), eval.getEndTime(), key.substring(1), "context");
				measureEntry.getValue().addAll(0,negatedContexSerie.collect(Collectors.toList()));
			});
			
			// unavailable
			Stream<TimelineMeasure> available = measureEntry.getValue().stream().filter(measure -> 
				measure.getLabel() == "system_available"
			).sorted((m1, m2) -> m1.compareTo(m2));
			
			Stream<TimelineMeasure> unavailable = getComplementar(available, eval.getEndTime(), "system_unavailable", "failure");
			measureEntry.getValue().addAll(unavailable.collect(Collectors.toList()));

			// sort measures
			System.out.println("sorting");
			measureEntry.getValue().sort(compareType);
			
			//set a plot index
			
			System.out.println("indexing");
			// plot index, ++ for each different label
			int lastIndex = 0;
			Map<String, Integer> indexes = new HashMap<>();
			for(TimelineMeasure measure: measureEntry.getValue()) {
				if(indexes.get(measure.getLabel()) == null) {
					indexes.put(measure.getLabel(), ++lastIndex);
				}
				
				measure.setIndex(indexes.get(measure.getLabel()));				
			}
						
			// append each measure time line format (label, start, end)
			measureEntry.getValue().forEach( measure ->{
				lineBuff.append(header);
				lineBuff.append(measure.getIndex()+ "\t");
				lineBuff.append(measure.getLabel()+ "\t");
				lineBuff.append(measure.getStart()+ "\t");
				lineBuff.append(measure.getEnd()+ "\t");
				lineBuff.append(measure.getType());
				// end of line
				//add to lines set ant reset linebuff to reuse
				lineBuff.append("\r\n");
				lines.add(lineBuff.toString());
				lineBuff.setLength(0); 
			});
		}
		return lines;
	}

	private Stream<TimelineMeasure> getComplementar(Stream<TimelineMeasure> measures, long endTime,
			String label, String type) {
		long lastEnd = 0;
	
		List<TimelineMeasure> complementar = new ArrayList<>();
		for(TimelineMeasure measure:measures.collect(Collectors.toList())) {
			if(lastEnd < measure.getStart()) {
				complementar.add(new TimelineMeasure(label, type, lastEnd, measure.getStart()));
				lastEnd = measure.getEnd();
			}
		}
		
		if(lastEnd < endTime) {
			complementar.add(new TimelineMeasure(label, type, lastEnd, endTime));
		}
		
		return complementar.stream();
	}
	
}
