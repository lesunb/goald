package goalp.evaluation.goals;

import java.util.stream.Stream;

import goald.evaluation.Evaluation;

public interface IReportResult<T extends Evaluation<?>> {

	void doReport(Stream<T> result);
	
	void addToReport(T evaluation);

	void flushAll();

	void start(int total);
	
	public void close();

}
