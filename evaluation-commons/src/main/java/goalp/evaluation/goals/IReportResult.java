package goalp.evaluation.goals;

import java.util.stream.Stream;

import goald.eval.exec.Evaluation;

public interface IReportResult {

	void doReport(Stream<Evaluation> result);

	void flushAll();

}
