package goald.evaluation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Named;

import goald.eval.exec.Evaluation;
import goald.evaluation.Measure;
import goalp.Conf;
import goalp.Conf.Keys;
import goalp.evaluation.goals.IReportResult;

@Named
public class ReportResultToFile implements IReportResult {

	private List<String> lines;
	
	public ReportResultToFile() {
		
	}
	
	@Override
	public void doReport(Stream<Evaluation> evaluations) {

		evaluations.forEach(evaluation -> {
			if(lines == null) {
				initLinesWithHeader(evaluation);
			}
			
			List<String> lines = getLines(evaluation);
			this.lines.addAll(lines);
		});
		
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
		
		Map<Integer, List<Measure>> indexedMesures  = eval.getIndexedMeasures();
		Integer firstKey = (Integer) indexedMesures.keySet().toArray()[0];
		
		List<Measure> firstSetOfMeasures = indexedMesures.get(firstKey);
		
		
		for(Measure measure: firstSetOfMeasures) {
			String measurelabel = measure.getLabel();
			sb.append(measurelabel + "\t");
		}
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
		for(Map.Entry<Integer,List<Measure>> measureEntry: eval.getIndexedMeasures().entrySet()) {
			
			// append all factors
			lineBuff.append(sb);
			
			// append execution index (id of a repetition with same factors)
			lineBuff.append(measureEntry.getKey()+ "\t");
			
			// append each measure
			measureEntry.getValue().forEach( measure ->{
				lineBuff.append(measure.getValue()+ "\t");
			});
			// end of line
			lineBuff.append("\r\n");
			
			//add to lines set ant reset linebuff to reuse
			lines.add(lineBuff.toString());
			lineBuff.setLength(0); 
		}
		return lines;
	}

	public void flushAll(){		
		flush(this.lines);
	}
	
	public void flush(List<String> lines){		
		//write lines to file 
		Path path = Paths.get( Conf.get(Keys.RESULT_FILE));
		path.toAbsolutePath();
		
		//TODO create first columns
		
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			for(String line: lines){
				writer.write(line);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
