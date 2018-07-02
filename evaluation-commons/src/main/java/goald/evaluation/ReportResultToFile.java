package goald.evaluation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import goald.eval.exec.Evaluation;
import goalp.Conf;
import goalp.Conf.Keys;
import goalp.evaluation.goals.IReportResult;

@Named
public class ReportResultToFile implements IReportResult {

	private List<String> lines;
	private BufferedWriter writer;
	private int writedLines = 0;
	private int pending = 0;
	private int total = 0;
	
	@Inject
	Timer timer;
	
	public ReportResultToFile() {
		
	}
	

	@Override
	public void start(int total) {
		this.pending = this.total = total;
		timer.begin();
	}


	@Override
	public void addToReport(Evaluation evaluation) {
		if(lines == null) {
			initLinesWithHeader(evaluation);
			this.initFile(evaluation);
		}
		
		List<String> lines = getLines(evaluation);
		this.lines.addAll(lines);
		this.flushAll();
	}
	
	@Override
	public void doReport(Stream<Evaluation> evaluations) {

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
		try {
			for(String line: lines){
				writer.write(line);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally{
			lines.clear();
		}
		if(writedLines++ >= 100) {
			pending -= 100;
			float miles  = timer.split("100")/1000000;
			float minutesToComplete = ((pending/100)*miles)/( 1000 * 60);
			System.out.println(" " + pending + "/" + total +" " + miles + " ( " + minutesToComplete + " min to end)" );
			writedLines = 0;
		}
		System.out.print(".");
	}
	
	public void initFile(Evaluation evaluation) {
		if(writer != null) {
			return;
		}
		
		String name = (String) evaluation.getConstants().get("resultFileName").toString();
		
		Path path = Paths.get( Conf.get(Keys.RESULT_DIR) + name + (new Date()).getTime());
		path.toAbsolutePath();
		
		//TODO create first columns
		try {
			writer = Files.newBufferedWriter(path);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void close() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
