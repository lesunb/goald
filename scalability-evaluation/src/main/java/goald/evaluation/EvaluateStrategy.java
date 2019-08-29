package goald.evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.eval.exec.Execution;
import goald.eval.spec.ExecSpec;
import goald.eval.spec.Experiment;
import goald.evaluation.exec.ExecuteExperiment;
import goald.evaluation.exec.ExecuteExperiment.ExecSetup;
import goald.evaluation.exec.ExecuteExperiment.SetupAndEvaluation;
import goald.evaluation.response.ReportResponseEvaluationToFile;
import goald.evaluation.response.ResponseEvaluation;
import goald.exputil.EvalUtil;
import goalp.evaluation.goals.IEvaluate;

public class EvaluateStrategy implements IEvaluate {

	@Inject
	ExecuteExperiment execute;
	
	@Inject
	ReportResponseEvaluationToFile report;
	
	List<SetupAndEvaluation> rank = new ArrayList<>();
	
	

	String [] factors = { "bundles_count" };
	String [] measures = { "dam_updated_0",  "dam_updated_1" };
	EvaluationRank eRank = new EvaluationRank(10, factors, measures);
	
	@Inject
	Logger log;
	
	int execIndex;
	@Override
	public void exec(List<Experiment> experiments){
		

		
		//exec experiments
		experiments.forEach( experiment -> {
			execute.setup(experiment);
			log.info("Experiment factor {}", EvalUtil.getFactors(experiment));
			
			//exec
			List<Execution> execs = experiment.getExecutions();
			int total = execs.size();
			report.start(total);		
			execs.stream()
			.map((exec) -> {
				int execId = 0;
				exec.getEvaluation().putFactor("mode", 0); //normal
				//setup and exec experiments
				ExecSpec spec = exec.getSpecification();
				execIndex = spec.getInteger("index");
				ExecSetup execSetup = execute.createSetup(spec,
						exec.getEvaluation());
				SetupAndEvaluation se = execute.execute(execSetup, exec.getEvaluation(), execIndex, 
						execIndex*1000 + execId++);
				++execId;
				
				//rank
				this.rankEvaluation(se);
				return exec.getEvaluation();
			})
			.forEach(report::addToReport);
			 
			//re-execute the top ranked (outliers)
			int mode = 1;
			eRank.getRanks()
				.forEach((key, rank) -> {
					//re exec outliners
					for(int i = 0; i< 10; i++) {
						rank.stream()
						.map((outlier) -> {
							ResponseEvaluation eval = outlier.getEvaluation().blankCopy();
							eval.putFactor("mode", key.contains("dam_updated_0") ? 1 : 2); // reexecute
							eval.putFactor("specIndex", outlier.getEvaluation().getFactors().get("specIndex"));
							execute.execute(outlier.getExecSetup(), eval, ++execIndex, outlier.getExecId());
							return eval;
						})
						.forEach(report::addToReport);
					}
				});
			report.close();
		});

 	}

	private void rankEvaluation(SetupAndEvaluation se) {
		eRank.rankEvaluation(se);
	}	
	
	
	private Collection<List<SetupAndEvaluation>> getTopRanked() {
		return eRank.getTopRanked();
	}
	
}
