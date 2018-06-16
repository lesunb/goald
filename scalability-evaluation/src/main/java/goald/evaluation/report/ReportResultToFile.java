package goald.evaluation.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import goald.evaluation.model.PlanningExperiment;
import goalp.Conf;
import goalp.Conf.Keys;

//@Named
public class ReportResultToFile {// implements IReportResult {


	//@Override
	public void exec(List<PlanningExperiment> experiments) {
		
		Path path = Paths.get( Conf.get(Keys.RESULT_FILE));
		
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			experiments.forEach(( exp) -> {
				//check experiment type
				
				
				experimentToString(writer, exp);
			});
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void experimentToString(BufferedWriter writer, PlanningExperiment exp){
		String experimentReport = exp.getRepoSpec() + ","
				+ "\n";
		try {
			
			writer.write(experimentReport);
			
		}catch(IOException e){
			throw new IllegalStateException(e);
		}
	}
	

}
