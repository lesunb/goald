package goald.evaluation.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Named;

import goald.evaluation.Measure;
import goald.evaluation.ReportToFileAbstract;

@Named
public class ReportResponseEvaluationToFile extends ReportToFileAbstract<ResponseEvaluation> {

	List<String> factorsKeys = new ArrayList<>();
	List<String> measuresKeys = new ArrayList<>();
	List<String> Keys = new ArrayList<>();
	
	private final String NA_str = "NA";
	
	public ReportResponseEvaluationToFile() {
		
	}

	@Override
	public void addToReport(ResponseEvaluation evaluation) {
		if(lines == null) {
			initLinesWithHeader(evaluation);
			this.initFile(evaluation);
		}
		
		List<String> lines = getLines(evaluation);
		this.lines.addAll(lines);
		this.flushAll();
		
	}
	
	@Override
	public void doReport(Stream<ResponseEvaluation> evaluations) {

		evaluations.forEach(this::addToReport);
		
	}
	
	private boolean initLinesWithHeader(ResponseEvaluation eval) {
		if(eval == null) {
			return false;
		}		
		StringBuffer sb = new StringBuffer();
		
		//collumnsKeys.add("execIndex");
		sb.append("execIndex\t");

		eval.getOrderedFactorList()
		.forEach(factor ->{
			factorsKeys.add(factor );
			sb.append(factor + "\t");
		});
		
		Map<Integer, List<Measure>> indexedMesures  = eval.getIndexedMeasures();
//		Integer firstKey = (Integer) indexedMesures.keySet().toArray()[0];		
//		List<Measure> firstSetOfMeasures = indexedMesures.get(firstKey);
//		
//		for(Measure measure: firstSetOfMeasures) {
//			String measurelabel = measure.getLabel();
//			
//			measuresKeys.add(measurelabel);
//			sb.append(measurelabel + "\t");
//		}
		eval.getOrderedMeasuresList()
		.forEach(key ->{
			measuresKeys.add(key );
			sb.append(key + "\t");
		});
		
		sb.append("\r\n");		
		this.lines = new ArrayList<>();
		lines.add(sb.toString());
		return true;
	}
	

	private List<String> getLines(ResponseEvaluation eval) {
		StringBuffer sb = new StringBuffer();
		
		List<String> lines = new ArrayList<>();
		factorsKeys
		.forEach(key ->{
			sb.append("\t" + toStrOrNa(eval.getFactors().get(key)));
		});
			
		StringBuffer lineBuff = new StringBuffer();
		for(Map.Entry<Integer, List<Measure>> measureEntry: eval.getIndexedMeasures().entrySet()) {			
			Integer execIndex = measureEntry.getKey();
			// append execution index (id of a repetition with same factors)
			lineBuff.append(execIndex);
			
			// append all factors
			lineBuff.append(sb);

			// append each measure
			Map<String, String> measuresMap = eval.getMeasuresMap(execIndex);
			measuresKeys.forEach(key -> {
				lineBuff.append("\t" + toStrOrNa(measuresMap.get(key)));
			});
			// end of line
			lineBuff.append("\r\n");
			
			//add to lines set ant reset linebuff to reuse
			lines.add(lineBuff.toString());
			lineBuff.setLength(0); 
		}
		return lines;
	}
	
	private String toStrOrNa(Object object) {
		return (object == null) ? NA_str:  object.toString();
	}
}
