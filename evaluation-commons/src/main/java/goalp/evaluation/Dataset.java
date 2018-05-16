package goalp.evaluation;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import goalp.Conf;
import goalp.Conf.Keys;

public class Dataset {

	List<String> lines;
	
	public static Dataset init(String title, String... columns){
		Dataset instance = new Dataset();
		instance.lines = new ArrayList<>();
		instance.log((Object[])columns);
		return instance;
	}
	
	public void log(Object... columns){
		StringBuilder sb = new StringBuilder();
		for(Object column:columns){
			sb.append(column.toString()+'\t'); 
		}
		this.lines.add(sb.toString()+"\r\n");
	}
	
	public void flush(){
		Path path = Paths.get( Conf.get(Keys.RESULT_FILE));
		path.toAbsolutePath();
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			for(String line: lines){
				writer.write(line);
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
