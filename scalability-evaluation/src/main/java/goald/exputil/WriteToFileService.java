package goald.exputil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import goald.eval.exec.ExecResult;
import goald.exputil.WriteService;
import goalp.Conf;
import goalp.Conf.Keys;

public class WriteToFileService extends WriteService{

	@Override
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
		if(result.getAgent() != null &&
				result.getAgent().getRootDame() != null) {
			printTree(result.getAgent().getRootDame(), 0, writer);
		}						
	}
}
