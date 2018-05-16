package goalp.exputil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import goalp.Conf;
import goalp.Conf.Keys;
import goalp.evaluation.model.ExecResult;
import goalp.model.Goal;

public class WriteToFileService extends WriteService{

	public void it(ExecResult result){
		it(result, Conf.get(Keys.DEPL_PLAN_FILE));
	}
	
	public void it(ExecResult result,  String pathStr) {
		
		Path path = Paths.get(pathStr);
		
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			write(result, writer);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private void write(ExecResult result, BufferedWriter writer) throws IOException{
		for(Goal goal: result.getRequest().getGoals()){
			printTree(goal, result.getResultPlan().getPlan(), 0, writer);			
		}
	}

}
