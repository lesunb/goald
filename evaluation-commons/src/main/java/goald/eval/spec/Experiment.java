package goald.eval.spec;

import java.util.ArrayList;
import java.util.List;

import goald.eval.exec.Evaluation;
import goald.eval.exec.Execution;

public class Experiment {
	
	public String name;
	
	private Evaluation evaluation;
		
	private List<Execution> executions;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Execution> getExecutions() {
		if(executions == null){
			executions = new ArrayList<Execution>();
		}
		return executions;
	}
	

	public void toExecute(Execution execution) {
		getExecutions().add(execution);
	}

	public Evaluation getEvaluation() {
		if(evaluation == null){
			evaluation = new Evaluation();
		}
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	

}
