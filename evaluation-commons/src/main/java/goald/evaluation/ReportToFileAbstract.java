package goald.evaluation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import goalp.Conf;
import goalp.Conf.Keys;
import goalp.evaluation.goals.IReportResult;

public abstract class ReportToFileAbstract<T extends Evaluation> implements IReportResult<T> {

	protected List<String> lines;
	private BufferedWriter writer;
	private int writedLines = 0;
	private int pending = 0;
	private int total = 0;
	@Inject
	ClockTimer timer;

	public ReportToFileAbstract() {
		super();
	}

	@Override
	public void start(int total) {
		this.pending = this.total = total;
		timer.begin();
	}

	public void flushAll() {		
		flush(this.lines);
	}

	public void flush(List<String> lines) {		
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